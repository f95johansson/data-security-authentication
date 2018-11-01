import org.junit.Assert;
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
        try {
            rmiPrinter = (RMIPrinter) Naming.lookup("rmi://localhost:8099/printer");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            System.err.println("Did you remember to start the server");
            System.exit(1);
        }
    }

    @Test
    public void should_return_the_value_set() throws RemoteException {
        final String settingsName = "cool_setting";
        final String value = "345 decibel";

        rmiPrinter.setConfig(settingsName, value);
        assertEquals(value, rmiPrinter.readConfig(settingsName));
    }
}