import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public class Client {
  
  public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
      RMIPrinter printer = (RMIPrinter) Naming.lookup("rmi://localhost:8099/printer");

      //this only works since they are sharing the same files, impossible to do remotely
      TheIdProvider theIdProvider = new TheIdProvider(new TheKeeperOfRecords(), new TheHasher());
      theIdProvider.addUser("hej", "password");
      String sessionKey = printer.logIn("hej", "password");

      printer.print("test.docx", "MyAwesomePrinter", sessionKey);

      printer.setConfig("Yo", "It works", sessionKey);
      String value = printer.readConfig("Yo", sessionKey);
      System.out.println(value);
  }
} 