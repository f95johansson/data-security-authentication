import BackendStuff.Mains.Backend;
import BackendStuff.Status;
import BackendStuff.Mains.UserRegistration;
import BackendStuff.Users;
import ClientStuff.Client;
import Interface.RMIPrinter;
import Roles.Role;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class ClientTest {

    private static RMIPrinter rmiPrinter;
    private static String sessionKey;

    @BeforeClass
    public static void SetPrinter() throws IOException {
        Backend.startServer(8081);

        Users keeper = new Users();
        if (!keeper.userWithNameExists("hej")) {
            UserRegistration userRegistration = new UserRegistration(keeper);
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