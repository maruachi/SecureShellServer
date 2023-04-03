import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SecureShellServer {

    public static final int PORT = 7777;

    public static void main(String[] args) {
        new SecureShellServer().run();
    }

    public void run() {
        ServerSocket serverSocket = createServerSocket();

        ClientListener clientListener = ClientListener.createByPort(PORT);

        //async로 작성 -> thread처리를 통해 sync로 돌리도록 한다.
        while (true) {
            Socket socket = clientListener.listen();

            Thread sshProcessThread = new Thread(()->sshProcess(socket));
            sshProcessThread.start();
        }
    }

    private void sshProcess(Socket socket) {
        try {
            BufferedReader reader = IoUtils.toReader(socket.getInputStream());
            Writer writer = IoUtils.toWriter(socket.getOutputStream());
            while (true) {
                String commandLine = reader.readLine();
                String[] commandLineElement = commandLine.split("[ ]+");
                int numElement = commandLineElement.length;

                if (numElement == 1 && "quit".equals(commandLineElement[0])) {
                    IoUtils.writeLine(writer, "ssh 접속을 종료합니다.");
                    return;
                }

                Command command = new EmptyCommand();
                if (numElement == 2 && "cat".equals(commandLineElement[0])) {
                    String filename = commandLineElement[1];

                    command = new Cat(filename, socket.getOutputStream());
                }

                if (numElement == 3 && "cp".equals(commandLineElement[0])) {
                    String sourceFilename = commandLineElement[1];
                    String targetFilename = commandLineElement[2];

                    command = new Copy(sourceFilename, targetFilename);
                }

                if (numElement == 3 && "mv".equals(commandLineElement[0])) {
                    String sourceFilename = commandLineElement[1];
                    String targetFilename = commandLineElement[2];

                    command = new Move(sourceFilename, targetFilename);
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
