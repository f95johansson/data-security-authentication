
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * This suite tests so no function can be called from the client without verification
 */
public class SecurityTest {

    private static RMIPrinter printer;
    @BeforeClass
    public static void StartUp() throws RemoteException, NotBoundException, MalformedURLException {
        UserRegistration userRegistration = new UserRegistration(new Users());
        String name = "validUser";
        String psw = "validPassw0rd";
        userRegistration.addUser(name, psw);

        //adds one real key to the system
        final int port = 8181;
        Backend.startServer(port);
        printer = Client.startClient(port);
        printer.logInSession(name, psw);
    }

    @Test(expected = RemoteException.class)
    public void print() throws RemoteException  {
        printer.print("hd", "hjf", "jkff");
    }

    @Test(expected = RemoteException.class)
    public void queue() throws RemoteException {
        printer.queue("fjksf0");
    }

    @Test(expected = RemoteException.class)
    public void topQueue() throws RemoteException {
        printer.topQueue(1, "djkd");
    }

    @Test(expected = RemoteException.class)
    public void start() throws RemoteException {
        printer.start("ffh00");
    }

    @Test(expected = RemoteException.class)
    public void stop() throws RemoteException {
        printer.stop("dhjd");
    }

    @Test(expected = RemoteException.class)
    public void restart() throws RemoteException {
        printer.restart("kfj");
    }

    @Test(expected = RemoteException.class)
    public void status() throws RemoteException {
        printer.status("djkd");
    }

    @Test(expected = RemoteException.class)
    public void readConfig() throws RemoteException {
        printer.readConfig("jkff", "hjhd");
    }

    @Test(expected = RemoteException.class)
    public void setConfig() throws RemoteException {
        printer.setConfig("djkd", "Jfk", "hkf");
    }
}
