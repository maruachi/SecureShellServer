import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Copy implements Command{
    private final String sourceFilename;
    private final String targetFilename;

    public Copy(String sourceFilename, String targetFilename) {
        this.sourceFilename = sourceFilename;
        this.targetFilename = targetFilename;
    }

    @Override
    public void execute() {
        if (Files.notExists(Paths.get(sourceFilename)) || Files.exists(Paths.get(targetFilename))) {
            return;
        }

        try {
            InputStream in = new FileInputStream(sourceFilename);
            OutputStream out = new FileOutputStream(targetFilename);

            IoUtils.transferAllByte(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
