
import java.io.OutputStream;
import java.io.PrintStream;

public class HelperFunctions {
    public static void suppressOutput() {
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {}
        }));
    }
}
