import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class PermissionsTest {

    private static RMIPrinter printer;
    @BeforeClass
    public static void StartUp() throws RemoteException, NotBoundException, MalformedURLException {
        final int port = 8181;
        Backend.startServer(port);
        printer = Client.startClient(port);
    }

    @Test
    public void should_allow_print_for_alice() throws RemoteException {
        String session = printer.logIn("Alice", "password");
        printer.print("file", "printer", session);
    }

    @Test(expected = RemoteException.class)
    public void should_not_allow_print_for_bob() throws RemoteException {
        String session = printer.logIn("Bob", "password");
        printer.print("file", "printer", session);
    }
}
