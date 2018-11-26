package Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * An RMI Interface for printer services
 */
public interface RMIPrinter extends Remote {
    int print(String filename, String printer, String sessionKey) throws RemoteException;

    String queue(String sessionKey) throws RemoteException;

    void topQueue(int job, String sessionKey) throws RemoteException;   // moves job to the top of the queue

    void start(String sessionKey) throws RemoteException;   // starts the print server

    void stop(String sessionKey) throws RemoteException;   // stops the print server

    void restart(String sessionKey) throws RemoteException;   // stops the print server, clears the print queue and starts the print server again

    String status(String sessionKey) throws RemoteException;  // prints currentStatus of printer on the user's display

    String readConfig(String parameter, String sessionKey) throws RemoteException;   // prints the value of the parameter on the user's display

    void setConfig(String parameter, String value, String sessionKey) throws RemoteException;   // sets the parameter to value

    //returns sessionkey
    String logIn(String username, String password) throws RemoteException;

    boolean changeMyPassword(String username, String oldPassword, String newPassword) throws RemoteException;
}
