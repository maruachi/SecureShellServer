import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SecureCopyClient implements Command{
    public static final int PORT = 7777;
    private final String ip;
    private final String serverFilename;
    private final String clientFilename;

    public SecureCopyClient(String ip, String serverFilename, String clientFilename) {
        this.ip = ip;
        this.serverFilename = serverFilename;
        this.clientFilename = clientFilename;
    }

    @Override
    public void execute() {
        if (Files.exists(Paths.get(clientFilename))) {
            return;
        }

        try (Socket socket = new Socket(ip, PORT);){
            Writer writer = IoUtils.toWriter(socket.getOutputStream());
            BufferedReader reader = IoUtils.toReader(socket.getInputStream());

            IoUtils.writeLine(writer, serverFilename);

            Response response = Response.create(reader.readLine());
            if (response == Response.FAIL) {
                return;
            }

            if (response == Response.OK) {
                IoUtils.transferAllByte(socket.getInputStream(), new FileOutputStream(clientFilename));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
