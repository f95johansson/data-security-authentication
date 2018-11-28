import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class PermissionsTest {

    private static final String[] admin = new String[] {
            "print",
            "queue",
            "topQueue",
            "start",
            "stop",
            "restart",
            "status",
            "readConfig",
            "setConfig"
    };

    private static final String[] maintainer = new String[] {
            "start",
            "stop",
            "restart",
            "status",
            "readConfig",
            "setConfig"
    };

    private static final String[] powerUser = new String[] {
            "topQueue",
            "restart",
            "stop",
            "print",
            "queue",
            "start"
    };

    private static final String[] user = new String[] {
            "print",
            "queue"
    };

    private static RMIPrinter printer;
    @BeforeClass
    public static void startUp() throws RemoteException, NotBoundException, MalformedURLException {
        final int port = 8181;
        Backend.startServer(port);
        printer = Client.startClient(port);

        // setup all the users
        UserRegistration userRegistration = new UserRegistration(new Users());
        userRegistration.addUser("Alice", "password");
        userRegistration.updateUserPermission("Alice", admin);
        userRegistration.addUser("Bob", "password");
        userRegistration.updateUserPermission("Bob", maintainer);
        userRegistration.addUser("Cecilia", "password");
        userRegistration.updateUserPermission("Cecilia", powerUser);
        userRegistration.addUser("David", "password");
        userRegistration.updateUserPermission("David", user);
        userRegistration.addUser("Erica", "password");
        userRegistration.updateUserPermission("Erica", user);
        userRegistration.addUser("Fred", "password");
        userRegistration.updateUserPermission("Fred", user);
        userRegistration.addUser("George", "password");
        userRegistration.updateUserPermission("George", user);
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
        String session = printer.logIn("Bob", "password");
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

    @Test
    public void should_allow_cecilia_to_queue_topQueue_stop_restart_print_start() throws RemoteException {
        String session = printer.logIn("Cecilia", "password");
        int job = printer.print("file", "printer", session);
        printer.queue(session);
        printer.topQueue(job, session);
        printer.start(session);
        printer.stop(session);
        printer.restart( session);
    }

    @Test(expected = RemoteException.class)
    public void should_not_allow_cecilia_to_status_readConfig_setConfig() throws RemoteException {
        String session = printer.logIn("Cecilia", "password");
        printer.status( session);
        printer.readConfig("test", session);
        printer.setConfig("test", "test", session);
    }
}
