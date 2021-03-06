import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class PrinterService extends UnicastRemoteObject implements RMIPrinter {

    private Map<String, String> settings = new HashMap<>();

    //map of printer -> job-queue
    private List<Job> jobs = new ArrayList<>();
    private Status currentStatus = Status.On;

    private GateKeeper gateKeeper = new GateKeeper(new Users());

    private int uniqueIDGenerator = 0;
    private final RemoteException NOT_LOGGED_IN_EXCEPTION = new RemoteException("You are not a valid user, please log in");

    public PrinterService() throws RemoteException {
        super();
    }

    private void log(String username, String method) {
        System.out.println("[" + username + ", " + method + "]");
    }

    private void log(String username, String method, String description) {
        System.out.println("[" + username + ", " + method + "]: " + description);
    }

    private String getUsernameOrThrowRemoteException(String key) throws RemoteException {
        String username = gateKeeper.validSessionKey(key);
        
        if (username == null)
            throw NOT_LOGGED_IN_EXCEPTION;

        return username;
    }

    @Override
    public int print(String filename, String printer, String sessionKey) throws RemoteException {
        String username = getUsernameOrThrowRemoteException(sessionKey);

        log(username, "PRINT", "(" + filename + ", " + printer + ")");
        return pushPrintJob(filename, printer);
    }

    private int pushPrintJob(String filename, String printer) {
        Job job = new Job(uniqueIDGenerator++, filename, printer);
        jobs.add(jobs.size(), job);
        return job.jobNumber;
    }

    @Override
    public String queue(String sessionKey) throws RemoteException {
        String username = getUsernameOrThrowRemoteException(sessionKey);
        log(username, "QUEUE");

        return jobs.stream()
                .map(job -> "<" + job.jobNumber + "> <" + job.fileName + ">")
                .reduce((a, b) -> a + "\n" + b)
                .orElse("[No jobs]");
    }

    @Override
    public void topQueue(int job, String sessionKey) throws RemoteException {
        String username = getUsernameOrThrowRemoteException(sessionKey);
        
        log(username, "TOP QUEUE", String.valueOf(job));

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

    @Override
    public void start(String sessionKey) throws RemoteException {
        String username = getUsernameOrThrowRemoteException(sessionKey);
        
        log(username, "START");
        currentStatus = Status.On;
    }

    @Override
    public void stop(String sessionKey) throws RemoteException {
        String username = getUsernameOrThrowRemoteException(sessionKey);
        
        log(username, "STOP");
        currentStatus = Status.Off;
    }

    @Override
    public void restart(String sessionKey) throws RemoteException {
        String username = getUsernameOrThrowRemoteException(sessionKey);
        
        log(username, "RESTART");
        stop(sessionKey);
        jobs.clear();
        start(sessionKey);
    }

    @Override
    public String status(String sessionKey) throws RemoteException {
        String username = getUsernameOrThrowRemoteException(sessionKey);
        
        log(username, "STATUS");
        return currentStatus.name();
    }

    @Override
    public String readConfig(String parameter, String sessionKey) throws RemoteException {
        String username = getUsernameOrThrowRemoteException(sessionKey);
        
        log(username, "READ CONFIG", parameter);
        return settings.getOrDefault(parameter, null);
    }

    @Override
    public void setConfig(String parameter, String value, String sessionKey) throws RemoteException {
        String username = getUsernameOrThrowRemoteException(sessionKey);
        
        log(username, "SET CONFIG", "(" + parameter + ", " + value + ")");
        settings.put(parameter, value);
    }

    @Override
    public String logIn(String username, String password) {
        return gateKeeper.startSession(username, password);
    }
}