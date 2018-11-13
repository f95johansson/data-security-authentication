import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public class Client {

    public static final String DEFAULT_USERNAME = "Cecilia";
    public static final String DEFAULT_PASSWORD = "Passw0rd123";

    private static void clearPasswords() {
        //this only works since they are sharing the same files, impossible to do remotely
        UserRegistration userRegistration = new UserRegistration(new Users());
        userRegistration.burnThePlace();
        userRegistration.addUser(DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    public static RMIPrinter startClient() throws RemoteException, NotBoundException, MalformedURLException {
        return startClient(8099);
    }

    public static RMIPrinter startClient(int port) throws RemoteException, NotBoundException, MalformedURLException {
        return (RMIPrinter) Naming.lookup("rmi://localhost:" + port + "/printer");
    }

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        clearPasswords();
        RMIPrinter printer = startClient();
        String sessionKey = printer.logIn(DEFAULT_USERNAME, DEFAULT_PASSWORD);

        printer.setConfig("magenta", "232", sessionKey);
        printer.readConfig("magenta", sessionKey);
        printer.status(sessionKey);
        printer.stop(sessionKey);
        printer.start(sessionKey);
        printer.print("unimportant", "dtu-808-printer", sessionKey);
        int job = printer.print("DTU.txt", "dtu-808-printer", sessionKey);
        printer.topQueue(job, sessionKey);
        printer.queue(sessionKey);
    }
} 