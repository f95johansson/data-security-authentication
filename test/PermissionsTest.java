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

    @Test
    public void should_allow_alice_to_print_queue_topQueue_start_stop_restart_status_readConfig_setConfig() throws RemoteException {
        String session = printer.logIn("Alice", "password");
        int job = printer.print("file", "printer", session);
        printer.queue(session);
        printer.topQueue(job, session);
        printer.start(session);
        printer.stop(session);
        printer.restart( session);
        printer.status( session);
        printer.readConfig("test", session);
        printer.setConfig("test", "test", session);
    }

    @Test
    public void should_allow_bob_to_start_stop_restart_status_readConfig_setConfig() throws RemoteException {
        String session = printer.logIn("Alice", "password");
        printer.start(session);
        printer.stop(session);
        printer.restart( session);
        printer.status( session);
        printer.readConfig("test", session);
        printer.setConfig("test", "test", session);
    }

    @Test(expected = RemoteException.class)
    public void should_not_allow_print_for_bob() throws RemoteException {
        String session = printer.logIn("Bob", "password");
        printer.print("file", "printer", session);
    }
}
