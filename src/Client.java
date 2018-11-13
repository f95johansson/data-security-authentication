import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

/**
 * Connects a client to a printer server
 */
public class Client {

    /**
     * Connect to a printer server (on localhost:8099)
     * @return the connected client
     */
    public static RMIPrinter startClient() throws RemoteException, NotBoundException, MalformedURLException {
        return startClient(8099);
    }

    /**
     * Connect to a printer server on localhost using the specified port
     * @return the connected client
     */
    public static RMIPrinter startClient(int port) throws RemoteException, NotBoundException, MalformedURLException {
        return (RMIPrinter) Naming.lookup("rmi://localhost:" + port + "/printer");
    }

    /**
     * A test main which connects to a printer (localhost:8099) with
     * a test username and password, and performs a print action.
     * Will only work if the specified user and password exists on server
     */
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        RMIPrinter printer = startClient();
        String sessionKey = printer.logIn("Cecilia", "Passw0rd123");
        printer.print("print.txt", "the-big-one", sessionKey);
    }
} 