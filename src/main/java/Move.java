import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Move implements Command{
    private final String sourceFilename;
    private final String targetFilename;

    public Move(String sourceFilename, String targetFilename) {
        this.sourceFilename = sourceFilename;
        this.targetFilename = targetFilename;
    }

    @Override
    public void execute() {
        new Copy(sourceFilename, targetFilename).execute();

        try {
            Files.delete(Paths.get(sourceFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
