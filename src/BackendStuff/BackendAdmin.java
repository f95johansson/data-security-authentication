package BackendStuff;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BackendAdmin {

    /**
     * Default configuration
     */
    private static void startServer() {
        startServer(8098);
    }

    /**
     * Starts a printer server that can be accessed on the specified port
     * via remote method invocation
     * @param port - The desired port
     */
    private static void startServer(int port) {
        try {
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("admin", new AdminService(new Users()));
            System.out.println("Admin-server started :)");
        } catch (RemoteException e) {
            System.out.println("Error while creating the server: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        startServer();
    }
}