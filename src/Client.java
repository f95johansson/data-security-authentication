import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public class Client {

    public static final String DEFAULT_USERNAME = "Cecilia";
    public static final String DEFAULT_PASSWORD = "Passw0rd123";

    private static void clearPasswords() {
        //this only works since they are sharing the same files, impossible to do remotely
        TheIdProvider theIdProvider = new TheIdProvider(new TheKeeperOfRecords(), new TheHasher());
        theIdProvider.burnThePlace();
        theIdProvider.addUser(DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        clearPasswords();

        RMIPrinter printer = (RMIPrinter) Naming.lookup("rmi://localhost:8099/printer");

        String sessionKey = printer.logIn(DEFAULT_USERNAME, DEFAULT_PASSWORD);

        printer.print("test.docx", "MyAwesomePrinter", sessionKey);

        printer.setConfig("Yo", "It works", sessionKey);
        String value = printer.readConfig("Yo", sessionKey);
        System.out.println(value);
    }
} 