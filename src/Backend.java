import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Backend {

    public static void startServer() {
        startServer(8099);
    }

    public static void startServer(int port) {
        try {
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("printer", new PrinterService());
            System.out.println("Server started :)");
        } catch (RemoteException e) {
            System.out.println("Error while creating the server: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        startServer();
    }
}