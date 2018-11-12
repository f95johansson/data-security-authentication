import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class PrinterService extends UnicastRemoteObject implements RMIPrinter {

    private Map<String, String> settings = new HashMap<>();
    private List<Job> jobs = new LinkedList<>();
    private Status currentStatus = Status.On;

    private Bouncer bouncer = new Bouncer(new Users());

    private int uniqueIDGenerator = 0;
    private final RemoteException NOT_LOGGED_IN = new RemoteException("You are not a valid user, please log in");

    public PrinterService() throws RemoteException {
        super();
    }

    private void log(String method) {
        System.out.println("[" + method + "]");
    }

    private void log(String method, String description) {
        System.out.println("[" + method + "]: " + description);
    }

    public void print(String filename, String printer, String sessionKey) throws RemoteException {
        if (!bouncer.validKey(sessionKey))
            throw NOT_LOGGED_IN;

        log("PRINT", "(" + filename + ", " + printer + ")");
        pushPrintJob(filename, printer);
    }

    private void pushPrintJob(String filename, String printer) {
        jobs.add(jobs.size(), new Job(uniqueIDGenerator++, filename, printer));
    }

    public String queue(String sessionKey) throws RemoteException {
        if (!bouncer.validKey(sessionKey))
            throw NOT_LOGGED_IN;
        
        log("QUEUE");

        return jobs.stream()
                .map(job -> "<" + job.jobNumber + "> <" + job.fileName + ">")
                .reduce((a, b) -> a + "\n" + b)
                .orElse("[No jobs]");
    }

    public void topQueue(int job, String sessionKey) throws RemoteException {
        if (!bouncer.validKey(sessionKey))
            throw NOT_LOGGED_IN;
        
        log("TOP QUEUE", String.valueOf(job));

        Job toMove = jobs.stream()
                .filter(j -> j.jobNumber == job)
                .findFirst()
                .orElse(null);

        if (toMove == null) {
            throw new RemoteException("No job with number " + job);
        }

        jobs.remove(toMove);
        jobs.add(0, toMove);
    }

    public void start(String sessionKey) throws RemoteException {
        if (!bouncer.validKey(sessionKey))
            throw NOT_LOGGED_IN;
        
        log("START");
        currentStatus = Status.On;
    }

    public void stop(String sessionKey) throws RemoteException {
        if (!bouncer.validKey(sessionKey))
            throw NOT_LOGGED_IN;
        
        log("STOP");
        currentStatus = Status.Off;
    }

    public void restart(String sessionKey) throws RemoteException {
        if (!bouncer.validKey(sessionKey))
            throw NOT_LOGGED_IN;
        
        log("RESTART");
        stop(sessionKey);
        jobs.clear();
        start(sessionKey);
    }

    public String status(String sessionKey) throws RemoteException {
        if (!bouncer.validKey(sessionKey))
            throw NOT_LOGGED_IN;
        
        log("STATUS");
        return currentStatus.name();
    }

    public String readConfig(String parameter, String sessionKey) throws RemoteException {
        if (!bouncer.validKey(sessionKey))
            throw NOT_LOGGED_IN;
        
        log("READ CONFIG", parameter);
        return settings.getOrDefault(parameter, null);
    }

    public void setConfig(String parameter, String value, String sessionKey) throws RemoteException {
        if (!bouncer.validKey(sessionKey))
            throw NOT_LOGGED_IN;
        
        log("SET CONFIG", "(" + parameter + ", " + value + ")");
        settings.put(parameter, value);
    }

    @Override
    public String logInSession(String username, String password) {
        return bouncer.startSession(username, password);
    }

    @Override
    public String logInOneTimeUse(String username, String password) {
        return bouncer.oneTimeKey(username, password);
    }
}