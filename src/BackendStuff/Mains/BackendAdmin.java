package BackendStuff.Mains;

import BackendStuff.Services.AdminService;
import BackendStuff.Users;
import org.jetbrains.annotations.NotNull;

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
    public static void startServer(int port) {
        startServer(port, new Users());
    }

    public static void startServer(int port, @NotNull Users users) {
        try {
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("admin", new AdminService(users));
            System.out.println("Admin-server started :)");
        } catch (RemoteException e) {
            System.out.println("Error while creating the server: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        startServer();
    }
}