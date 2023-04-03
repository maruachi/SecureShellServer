import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Cat implements Command {

    private final String filename;
    private final OutputStream out;

    public Cat(String filename, OutputStream out) {
        this.filename = filename;
        this.out = out;
    }

    @Override
    public void execute() {
        if (Files.notExists(Paths.get(filename))) {
            return;
        }

        try {
            InputStream in = new FileInputStream(filename);
            IoUtils.transferAllByte(in, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
