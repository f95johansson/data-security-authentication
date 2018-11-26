import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class HelperFunctions {

    public static Path createTempFolder() throws IOException {
        return createTempFolder(Paths.get("./temp"));
    }

    private static Path createTempFolder(Path path) throws IOException {
        if (!Files.exists(path)) {
            return Files.createDirectory(path);
        } else {
            return createTempFolder(Paths.get(path.toString() + "1"));
        }
    }

    public static Path createTempFile() {
        StringBuilder path = new StringBuilder("tempFile");
        Path temp = Paths.get(path + ".txt");

        while(Files.exists(temp)) {
            path.append("1");
            temp = Paths.get(path.toString() + ".txt");
        }

        return temp;
    }

    public static void removeTempFile(Path path) throws IOException {
        Files.delete(path);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void removeTempFolder(Path tempDirectory) throws IOException {
        Files.walk(tempDirectory)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

        Files.delete(tempDirectory);
    }

    public static void suppressOutput() {
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {}
        }));
    }
}
