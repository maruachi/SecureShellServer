import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientListener {
    private final ServerSocket serverSocket;

    private ClientListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public static ClientListener createByPort(int port) {
        try {
            return new ClientListener(new ServerSocket(port));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Socket listen() {
        while (true) {
            try {
                return serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
