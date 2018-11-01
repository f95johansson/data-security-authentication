import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class ClientTest {

    private static RMIPrinter rmiPrinter;

    @BeforeClass
    public static void SetPrinter() {
        Backend.StartServer();

        try {
            rmiPrinter = (RMIPrinter) Naming.lookup("rmi://localhost:8099/printer");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void Reset() throws RemoteException {
        rmiPrinter.restart();
    }

    @Test
    public void should_return_the_value_set() throws RemoteException {
        final String settingsName = "cool_setting";
        final String value = "345 decibel";

        rmiPrinter.setConfig(settingsName, value);
        assertEquals(value, rmiPrinter.readConfig(settingsName));
    }

    @Test
    public void should_return_all_jobs() throws RemoteException {
        final String fileName = "elvis_still_alive_proof.png";
        final String fileName2 = "ufo_proof.png";
        final String printerName = "some_printer";

        rmiPrinter.print(fileName, printerName);
        rmiPrinter.print(fileName2, printerName);

        String[] queue = rmiPrinter.queue().split("\n");
        assertTrue(queue[0].contains(fileName));
        assertTrue(queue[1].contains(fileName2));
    }

    @Test
    public void should_change_state() throws RemoteException {
        rmiPrinter.stop();
        assertEquals(Status.Off.name(), rmiPrinter.status());
        rmiPrinter.start();
        assertEquals(Status.On.name(), rmiPrinter.status());
    }
}