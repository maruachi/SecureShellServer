import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class SecureShellServer {

    public static final int PORT = 7777;

    public static void main(String[] args) {
        new SecureShellServer().run();
    }

    public void run() {
        ClientListener clientListener = ClientListener.createByPort(PORT);
        LoginManger loginManger = LoginManger.createDefault();
        loginManger.addUser("dong", "123");

        //async로 작성 -> thread처리를 통해 sync로 돌리도록 한다.
        while (true) {
            Socket socket = clientListener.listen();

            try {
                BufferedReader reader = IoUtils.toReader(socket.getInputStream());
                String sshLine = reader.readLine();
                String[] sshLineElement = sshLine.split("[ ]+");
                if (sshLineElement.length != 2) {
                    socket.close();
                    continue;
                }

                String username = sshLineElement[0];
                String password = sshLineElement[1];

                //login 실패
                Writer writer = IoUtils.toWriter(socket.getOutputStream());
                if (loginManger.isNotLogin(username, password)) {
                    IoUtils.writeLine(writer, "FAIL");
                    IoUtils.writeLine(writer, "authentication failed");
                    socket.close();
                    continue;
                }

                IoUtils.writeLine(writer, "OK");
                //login 성공
                //socket의 자원 해제는 sshProccess가 담당하고 있다.
                Thread sshProcessThread = new Thread(()->sshProcess(socket));
                sshProcessThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sshProcess(Socket socket) {
        try (socket){
            System.out.println("run sshProcess()");
            BufferedReader reader = IoUtils.toReader(socket.getInputStream());
            Writer writer = IoUtils.toWriter(socket.getOutputStream());
            while (true) {
                String commandLine = reader.readLine();
                System.out.println(commandLine);
                String[] commandLineElement = commandLine.split("[ ]+");
                int numElement = commandLineElement.length;
                if (numElement < 1) {
                    continue;
                }

                CommandName commandName = CommandName.createByValue(commandLineElement[0]);

                if (numElement == 1 && commandName == CommandName.QUIT) {
                    IoUtils.writeLine(writer, Response.FINISH.getValue());
                    IoUtils.writeLine(writer, "ssh 접속을 종료합니다.");
                    return;
                }

                Command command = new EmptyCommand();
                if (numElement == 2 && commandName == CommandName.CAT) {
                    String filename = commandLineElement[1];

                    command = new Cat(filename, socket.getOutputStream());
                }

                if (numElement == 3 && commandName == CommandName.COPY) {
                    String sourceFilename = commandLineElement[1];
                    String targetFilename = commandLineElement[2];

                    command = new Copy(sourceFilename, targetFilename);
                }

                if (numElement == 3 && commandName == CommandName.MOVE) {
                    String sourceFilename = commandLineElement[1];
                    String targetFilename = commandLineElement[2];

                    command = new Move(sourceFilename, targetFilename);
                }

                if (numElement == 4 && commandName == CommandName.SCP) {
                    String ip = commandLineElement[1];
                    String serverFilename = commandLineElement[2];
                    String clientFilename = commandLineElement[3];

                    command = new SecureCopyClient(ip, serverFilename, clientFilename);
                }

                command.execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ServerSocket createServerSocket() {
        try {
            return new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
