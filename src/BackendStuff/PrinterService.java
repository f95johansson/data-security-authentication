package BackendStuff;

import Interface.RMIPrinter;
import Roles.Function;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static Roles.Function.*;


public class PrinterService extends UnicastRemoteObject implements RMIPrinter {

    private Map<String, String> settings = new HashMap<>();

    private List<Job> jobs = new ArrayList<>();
    private Status currentStatus = Status.On;

    private GateKeeper gateKeeper = new GateKeeper(new Users());

    private int uniqueIDGenerator = 0;
    private final RemoteException NOT_LOGGED_IN_EXCEPTION =
            new RemoteException("You are not authorized for this function, maybe log in or request a role with more access");

    public PrinterService() throws RemoteException {
        super();
    }

    private void log(User user, String method) {
        System.out.println("[" + user.name + ", " + method + "]");
    }

    private void log(User user, String method, String description) {
        System.out.println("[" + user.name + ", " + method + "]: " + description);
    }

    private User getUsernameOrThrowRemoteException(String sessionKey, Function function) throws RemoteException {
        User user = gateKeeper.validSessionKey(sessionKey, function);
        
        if (user == null)
            throw NOT_LOGGED_IN_EXCEPTION;

        return user;
    }

    private int pushPrintJob(String filename, String printer) {
        Job job = new Job(uniqueIDGenerator++, filename, printer);
        jobs.add(jobs.size(), job);
        return job.jobNumber;
    }

    @Override
    public int print(String filename, String printer, String sessionKey) throws RemoteException {
        User user = getUsernameOrThrowRemoteException(sessionKey, PRINT);

        log(user, "PRINT", "(" + filename + ", " + printer + ")");
        return pushPrintJob(filename, printer);
    }

    @Override
    public String queue(String sessionKey) throws RemoteException {
        User user = getUsernameOrThrowRemoteException(sessionKey, QUEUE);
        log(user, "QUEUE");

        return jobs.stream()
                .map(job -> "<" + job.jobNumber + "> <" + job.fileName + ">")
                .reduce((a, b) -> a + "\n" + b)
                .orElse("[No jobs]");
    }

    @Override
    public void topQueue(int job, String sessionKey) throws RemoteException {
        User user = getUsernameOrThrowRemoteException(sessionKey, TOP_QUEUE);
        
        log(user, "TOP QUEUE", String.valueOf(job));

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
        User user = getUsernameOrThrowRemoteException(sessionKey, START);
        
        log(user, "START");
        currentStatus = Status.On;
    }

    @Override
    public void stop(String sessionKey) throws RemoteException {
        User user = getUsernameOrThrowRemoteException(sessionKey, STOP);
        
        log(user, "STOP");
        currentStatus = Status.Off;
    }

    @Override
    public void restart(String sessionKey) throws RemoteException {
        User user = getUsernameOrThrowRemoteException(sessionKey, RESTART);
        
        log(user, "RESTART");
        stop(sessionKey);
        jobs.clear();
        start(sessionKey);
    }

    @Override
    public String status(String sessionKey) throws RemoteException {
        User user = getUsernameOrThrowRemoteException(sessionKey, STATUS);
        
        log(user, "STATUS");
        return currentStatus.name();
    }

    @Override
    public String readConfig(String parameter, String sessionKey) throws RemoteException {
        User user = getUsernameOrThrowRemoteException(sessionKey, READ_CONFIG);
        
        log(user, "READ CONFIG", parameter);
        return settings.getOrDefault(parameter, null);
    }

    @Override
    public void setConfig(String parameter, String value, String sessionKey) throws RemoteException {
        User user = getUsernameOrThrowRemoteException(sessionKey, SET_CONFIG);
        
        log(user, "SET CONFIG", "(" + parameter + ", " + value + ")");
        settings.put(parameter, value);
    }

    @Override
    public String logIn(String username, String password) {
        return gateKeeper.startSession(username, password);
    }
}