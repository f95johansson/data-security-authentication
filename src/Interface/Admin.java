package Interface;

import Roles.Role;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * An RMI Interface for printer services
 */
public interface Admin extends Remote {
    void addUser(String username, String password, Role role, String sessionKey) throws RemoteException;
    void removeUser(String username, String sessionKey) throws RemoteException;
    void changeUserRole(String username, Role newRole, String sessionKey) throws RemoteException;
    String logInAsAdmin(String username, String password) throws RemoteException;
    Role lookUpUserRole(String username, String sessionKey) throws RemoteException;
}
