package BackendStuff.Functionality;

import BackendStuff.NonNullOrException;
import BackendStuff.StringOrException;
import BackendStuff.Mains.UserRegistration;
import BackendStuff.Users;
import Roles.Role;

import java.io.IOException;

public class AdminFunctions {

    private final Users users;
    
    public AdminFunctions(Users users) {
        this.users = users;
    }

    public boolean addUser(StringOrException username, StringOrException password, NonNullOrException<Role> role) {
        UserRegistration usr = new UserRegistration(users);
        return usr.addUser(username.value, password.value, role.value);
    }

    public boolean removeUser(StringOrException username) {
        try {
            users.removeUser(username.value);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeUserRole(StringOrException username, NonNullOrException<Role> newRole) {
        try {
            users.updateUserRole(username.value, newRole.value);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
