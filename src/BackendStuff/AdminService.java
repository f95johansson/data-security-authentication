package BackendStuff;

import Interface.Admin;
import static Roles.Function.*;
import Roles.Function;
import Roles.Role;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AdminService extends UnicastRemoteObject implements Admin {

    private final GateKeeper gateKeeper;
    private final Users users;

    public AdminService(Users users) throws RemoteException {
        super();
        this.users = users;
        this.gateKeeper = new GateKeeper(users);
    }

    private final RemoteException NO_ADMIN_EXCEPTION = new RemoteException("You are not logged as an admin");

    private void assertUserAdmin(String sessionKey, Function function) throws RemoteException {
        User user = gateKeeper.validSessionKey(sessionKey, function);
        if (user == null) throw NO_ADMIN_EXCEPTION;
    }


    @Override
    public void addUser(String username, String password, Role role, String sessionKey) throws RemoteException {
        assertUserAdmin(sessionKey, ADD_USER);
        UserRegistration usr = new UserRegistration(users);
        usr.addUser(username, password, role);
    }

    @Override
    public void removeUser(String username, String sessionKey) throws RemoteException {
        assertUserAdmin(sessionKey, REMOVE_USER);
        try {
            users.removeUser(username);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RemoteException("Could not remove the user for some reason, check backend-log");
        }
    }

    @Override
    public void changeUserRole(String username, Role newRole, String sessionKey) throws RemoteException{
        assertUserAdmin(sessionKey, CHANGE_USER_ROLE);
        try {
            users.updateUserRole(username, newRole);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RemoteException("Could not change user role for some reason, check backend-log");
        }
    }

    @Override
    public String logInAsAdmin(String username, String password) throws RemoteException {
        String sessionKey = gateKeeper.startSession(username, password);
        if (sessionKey == null) return null;

        User user = gateKeeper.getUser(sessionKey);
        if (user == null) return null;

        if (user.role != Role.ADMIN) return null;

        return sessionKey;
    }
}
