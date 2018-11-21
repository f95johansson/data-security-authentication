package BackendStuff.Services;

import BackendStuff.*;
import BackendStuff.Functionality.PrinterFunctions;
import Interface.RMIPrinter;
import Roles.Method;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static Roles.Method.*;

public class PrinterService extends UnicastRemoteObject implements RMIPrinter {

    private GateKeeper gateKeeper = new GateKeeper(new Users());

    private final RemoteException NOT_LOGGED_IN_EXCEPTION =
            new RemoteException("You are not authorized for this function, maybe log in or request a role with more access");

    private PrinterFunctions pf = new PrinterFunctions();

    public PrinterService() throws RemoteException {
        super();
    }

    private void log(User user, Method method) {
        System.out.println("[" + user.name + ", " + method.name() + "]");
    }

    private void log(User user, Method method, String description) {
        System.out.println("[" + user.name + ", " + method.name() + "]: " + description);
    }

    private User getUserOrThrowRemoteException(String sessionKey, Method method) throws RemoteException {
        User user = gateKeeper.validSessionKey(sessionKey, method);
        
        if (user == null)
            throw NOT_LOGGED_IN_EXCEPTION;

        return user;
    }


    @Override
    public int print(String filename, String printer, String sessionKey) throws RemoteException {
        User user = getUserOrThrowRemoteException(sessionKey, PRINT);

        log(user, PRINT, "(" + filename + ", " + printer + ")");
        return pf.print(new StringOrException(filename), new StringOrException(printer));
    }

    @Override
    public String queue(String sessionKey) throws RemoteException {
        User user = getUserOrThrowRemoteException(sessionKey, QUEUE);
        log(user, QUEUE);

        return pf.queue();
    }

    @Override
    public void topQueue(int job, String sessionKey) throws RemoteException {
        User user = getUserOrThrowRemoteException(sessionKey, TOP_QUEUE);
        
        log(user, TOP_QUEUE, String.valueOf(job));

        boolean foundValue = pf.topQueue(job);

        if (!foundValue) throw new RemoteException("Job not found");
    }

    @Override
    public void start(String sessionKey) throws RemoteException {
        User user = getUserOrThrowRemoteException(sessionKey, START);
        
        log(user, START);

        pf.start();
    }

    @Override
    public void stop(String sessionKey) throws RemoteException {
        User user = getUserOrThrowRemoteException(sessionKey, STOP);
        
        log(user, STOP);
        pf.stop();
    }

    @Override
    public void restart(String sessionKey) throws RemoteException {
        User user = getUserOrThrowRemoteException(sessionKey, RESTART);
        log(user, RESTART);

        pf.restart();
    }

    @Override
    public String status(String sessionKey) throws RemoteException {
        User user = getUserOrThrowRemoteException(sessionKey, STATUS);
        log(user, STATUS);

        return pf.status();
    }

    @Override
    public String readConfig(String parameter, String sessionKey) throws RemoteException {
        User user = getUserOrThrowRemoteException(sessionKey, READ_CONFIG);
        log(user, READ_CONFIG, parameter);

        return pf.readConfig(new StringOrException(parameter));
    }

    @Override
    public void setConfig(String parameter, String value, String sessionKey) throws RemoteException {
        User user = getUserOrThrowRemoteException(sessionKey, SET_CONFIG);
        log(user, SET_CONFIG, "(" + parameter + ", " + value + ")");

        pf.setConfig(new StringOrException(parameter), new StringOrException(value));
    }

    @Override
    public String logIn(String username, String password) throws RemoteException {
        if (username == null || password == null) throw new RemoteException("Input cannot be null");

        return gateKeeper.startSession(username, password);
    }
}