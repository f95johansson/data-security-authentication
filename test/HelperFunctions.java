
import java.io.OutputStream;
import java.io.PrintStream;

public class HelperFunctions {

    private static PrintStream defaultOut = System.out;

    public static void suppressOutput() {
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {}
        }));
    }

    public static void restoreOutput() {
        System.setOut(defaultOut);
    }
}
