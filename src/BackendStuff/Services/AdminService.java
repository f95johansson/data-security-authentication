package BackendStuff.Services;

import BackendStuff.*;
import BackendStuff.Functionality.AdminFunctions;
import BackendStuff.SafeTypes.NonNull;
import BackendStuff.SafeTypes.NonNullString;
import Interface.Admin;
import static Roles.Method.*;
import Roles.Method;
import Roles.Role;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AdminService extends UnicastRemoteObject implements Admin {

    private final AdminFunctions af;

    private final GateKeeper gateKeeper;

    public AdminService(Users users) throws RemoteException {
        super();
        af = new AdminFunctions(users);
        gateKeeper = new GateKeeper(users);
    }

    private final RemoteException NO_ADMIN_EXCEPTION = new RemoteException("You are not logged as an admin");

    private void assertUserIsAdmin(String sessionKey, Method method) throws RemoteException {
        User user = gateKeeper.validSessionKey(sessionKey, method);
        if (user == null) throw NO_ADMIN_EXCEPTION;
    }

    @Override
    public void addUser(String username, String password, Role role, String sessionKey) throws RemoteException {
        assertUserIsAdmin(sessionKey, ADD_USER);
        boolean succeeded = af.addUser(new NonNullString(username), new NonNullString(password), new NonNull<>(role));

        if (!succeeded)
            throw new RemoteException("Username is taken");
    }

    @Override
    public void removeUser(String username, String sessionKey) throws RemoteException {
        assertUserIsAdmin(sessionKey, REMOVE_USER);

        boolean succeeded = af.removeUser(new NonNullString(username));

        if (!succeeded)
            throw new RemoteException("Could not remove the user for some reason, check backend-log");
    }

    @Override
    public void changeUserRole(String username, Role newRole, String sessionKey) throws RemoteException {
        assertUserIsAdmin(sessionKey, CHANGE_USER_ROLE);

        boolean succeeded = af.changeUserRole(new NonNullString(username), new NonNull<>(newRole));

        if (!succeeded)
            throw new RemoteException("Could not change the users role for some reason, check backend-log");
    }

    @Override
    public Role lookUpUserRole(String username, String sessionKey) throws RemoteException {
        assertUserIsAdmin(sessionKey, LOOK_UP_USER_ROLE);
        return af.lookUpUserRole(username);
    }

    @Override
    public String logInAsAdmin(String username, String password) {
        if (username == null || password == null) return null;

        String sessionKey = gateKeeper.startSession(username, password);
        if (sessionKey == null) return null;

        User user = gateKeeper.getUser(sessionKey);
        if (user == null) return null;

        if (user.role != Role.ADMIN) return null;

        return sessionKey;
    }
}
