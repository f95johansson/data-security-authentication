import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public class Client {

    public static RMIPrinter startClient() throws RemoteException, NotBoundException, MalformedURLException {
        return startClient(8099);
    }

    public static RMIPrinter startClient(int port) throws RemoteException, NotBoundException, MalformedURLException {
        return (RMIPrinter) Naming.lookup("rmi://localhost:" + port + "/printer");
    }

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        RMIPrinter printer = startClient();
        String sessionKey = printer.logIn("Cecilia", "Passw0rd123");
        printer.print("print.txt", "the-big-one", sessionKey);
    }
} 