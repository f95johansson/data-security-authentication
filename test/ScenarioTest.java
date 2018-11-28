import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

public class ScenarioTest {

    private static RMIPrinter printer;
    private static final String[] technicianResponsibilities = new String[] {
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

    private static final String[] userPermissions = new String[] {
            "print",
            "queue"
    };

    @BeforeClass
    public static void startUp() throws RemoteException, NotBoundException, MalformedURLException {
        final int port = 8181;
        Backend.startServer(port);
        printer = Client.startClient(port);
    }

    @Test
    public void should_add_henry() {
        UserRegistration userRegistration = new UserRegistration(new Users());

        userRegistration.addUser("Henry", "password");
        userRegistration.updateUserPermission("Henry", userPermissions);

        // Sort the arrays to be able to compare them
        assertArrayEquals(
                Arrays.stream(userPermissions).sorted().toArray(String[]::new),
                Arrays.stream(userRegistration.getUserPermissions("Henry")).sorted().toArray(String[]::new));
    }

    @Test
    public void should_add_ida_as_power_user() {
        UserRegistration userRegistration = new UserRegistration(new Users());

        userRegistration.addUser("Ida", "password");
        userRegistration.updateUserPermission("Ida", powerUser);

        // compare with Cecilia
        assertArrayEquals(
                Arrays.stream(userRegistration.getUserPermissions("Cecilia")).sorted().toArray(String[]::new),
                Arrays.stream(userRegistration.getUserPermissions("Ida")).sorted().toArray(String[]::new));
    }


    @Test
    public void should_transfer_technician_responsibilities_from_bob_to_george() {
        UserRegistration userRegistration = new UserRegistration(new Users());

        // set up scenario
        userRegistration.addUser("Bob", "password");
        userRegistration.addUser("George", "password");
        userRegistration.updateUserPermission("Bob", technicianResponsibilities);
        userRegistration.updateUserPermission("George", userPermissions);

        // transfer permissions
        String[] permissions = userRegistration.getUserPermissions("Bob");
        userRegistration.updateUserPermission("George", permissions);
        userRegistration.removeUser("Bob");

        assertFalse(userRegistration.userExists("Bob"));
        assertArrayEquals(
                Arrays.stream(technicianResponsibilities).sorted().toArray(String[]::new),
                Arrays.stream(userRegistration.getUserPermissions("George")).sorted().toArray(String[]::new));
    }


}
