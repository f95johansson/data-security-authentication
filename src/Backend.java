import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Backend {
  
  public static void main(String args[]) {
      try {
        Registry registry = LocateRegistry.createRegistry(8099);
        registry.rebind("printer", new PrinterService());
      } catch(RemoteException e) {
        System.out.println("Erroor while creating the server");
      }
  }
  
}