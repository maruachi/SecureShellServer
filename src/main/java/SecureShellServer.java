import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class SecureShellServer {

    public static final int PORT = 7777;

    public void run() {
        ServerSocket serverSocket = createServerSocket();

        //async로 작성 -> thread처리를 통해 sync로 돌리도록 한다.
        while (true) {
            try (Socket socket = serverSocket.accept();){
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
                    }

                    if (numElement == 3 && "cp".equals(commandLineElement[0])) {
                    }

                    if (numElement == 3 && "mv".equals(commandLineElement[0])) {
                    }

                    command.execute();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
