import BackendStuff.Mains.Backend;
import BackendStuff.Status;
import BackendStuff.UserRegistration;
import BackendStuff.Users;
import ClientStuff.Client;
import Interface.RMIPrinter;
import Roles.Role;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class ClientFunctionalityTest {

    private static RMIPrinter rmiPrinter;
    private static String sessionKey;

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    private static int unique = 0;

    @BeforeClass
    public static void SetPrinter() throws IOException {
        Users users = new Users(folder.newFile(unique++ + ".txt").getAbsolutePath());
        Backend.startServer(8081, users);

        if (!users.userWithNameExists("hej")) {
            UserRegistration userRegistration = new UserRegistration(users);
            userRegistration.addUser("hej", "password", Role.ADMIN);
        }

        try {
            rmiPrinter = Client.startClient(8081);
            sessionKey = rmiPrinter.logIn("hej", "password");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void Reset() throws RemoteException {
        rmiPrinter.restart(sessionKey);
    }

    @Test
    public void should_return_the_value_set() throws RemoteException {
        final String settingsName = "cool_setting";
        final String value = "345 decibel";

        rmiPrinter.setConfig(settingsName, value, sessionKey);
        assertEquals(value, rmiPrinter.readConfig(settingsName, sessionKey));
    }

    @Test
    public void should_return_all_jobs() throws RemoteException {
        final String fileName = "elvis_still_alive_proof.png";
        final String fileName2 = "ufo_proof.png";
        final String printerName = "some_printer";

        rmiPrinter.print(fileName, printerName, sessionKey);
        rmiPrinter.print(fileName2, printerName, sessionKey);

        String[] queue = rmiPrinter.queue(sessionKey).split("\n");
        assertTrue(queue[0].contains(fileName));
        assertTrue(queue[1].contains(fileName2));
    }

    @Test
    public void should_change_state() throws RemoteException {
        rmiPrinter.stop(sessionKey);
        assertEquals(Status.Off.name(), rmiPrinter.status(sessionKey));
        rmiPrinter.start(sessionKey);
        assertEquals(Status.On.name(), rmiPrinter.status(sessionKey));
    }
}