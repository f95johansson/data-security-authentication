import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Backend {

    /**
     * Default configuration
     */
    private static void startServer() {
        startServer(8099);
    }

    /**
     * Starts a printer server that can be accessed on the specified port
     * via remote method invocation
     * @param port - The desired port
     */
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