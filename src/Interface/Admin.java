package Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * An RMI Interface for printer services
 */
public interface Admin extends Remote {
    void addUser(String username, String password, String role, String sessionKey) throws RemoteException;
    void removeUser(String username, String sessionKey) throws RemoteException;
    void changeUserRole(String username, String newRole, String sessionKey) throws RemoteException;
    String logInAsAdmin(String username, String password) throws RemoteException;
    String lookUpUserRole(String username, String sessionKey) throws RemoteException;
}
