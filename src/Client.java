import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public class Client {
  
  public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
      RMIPrinter printer = (RMIPrinter) Naming.lookup("rmi://localhost:8099/printer");
      printer.print("test.docx", "MyAwesomePrinter");

      String value = printer.readConfig("Yo");
      System.out.println(value);
  }
} 