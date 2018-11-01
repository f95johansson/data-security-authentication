import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Backend {

  public static void StartServer() {
      try {
          Registry registry = LocateRegistry.createRegistry(8099);
          registry.rebind("printer", new PrinterService());
          System.out.println("Server started :)");
      } catch(RemoteException e) {
          System.out.println("Error while creating the server");
      }
  }
  
  public static void main(String args[]) {
      StartServer();
  }
}