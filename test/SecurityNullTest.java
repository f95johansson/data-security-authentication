
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * This suite tests that null can never be subsituted for the key for any value
 */
public class SecurityNullTest {

    private static RMIPrinter printer;

    @BeforeClass
    public static void StartUp() throws RemoteException, NotBoundException, MalformedURLException {
        UserRegistration userRegistration = new UserRegistration(new MockUsers());
        String name = "validUser";
        String psw = "validPassw0rd";
        userRegistration.addUser(name, psw);

        //adds one real key to the system
        final int port = 8080;
        Backend.startServer(port);
        printer = Client.startClient(port);
        printer.logIn(name, psw);
    }

    @Test(expected = RemoteException.class)
    public void print() throws RemoteException  {
        printer.print("hd", "hjf", null);
    }

    @Test(expected = RemoteException.class)
    public void queue() throws RemoteException {
        printer.queue("fjksf0");
    }

    @Test(expected = RemoteException.class)
    public void topQueue() throws RemoteException {
        printer.topQueue(1, null);
    }

    @Test(expected = RemoteException.class)
    public void start() throws RemoteException {
        printer.start("ffh00");
    }

    @Test(expected = RemoteException.class)
    public void stop() throws RemoteException {
        printer.stop(null);
    }

    @Test(expected = RemoteException.class)
    public void restart() throws RemoteException {
        printer.restart(null);
    }

    @Test(expected = RemoteException.class)
    public void status() throws RemoteException {
        printer.status(null);
    }

    @Test(expected = RemoteException.class)
    public void readConfig() throws RemoteException {
        printer.readConfig("jkff", null);
    }

    @Test(expected = RemoteException.class)
    public void setConfig() throws RemoteException {
        printer.setConfig("djkd", "Jfk", null);
    }
}
