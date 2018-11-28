import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Provides registering a new user functionality
 */
public class UserRegistration {
    private Users keeper;

    public UserRegistration(Users keeper) {
        this.keeper = keeper;
    }

    /**
     * Register a new user
     * @return True if successfull, false if failed or user already exists
     */
    public boolean addUser(String username, String password) {
        try {
            if (keeper.userWithNameExists(username)) return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        String salt;
        String hashedPassword;
        try {
            salt = Crypto.generateSalt();
            hashedPassword = Crypto.toHash(password, salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }

        try {
            keeper.addUser(new User(username, salt, hashedPassword, new HashSet<>()));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean userExists(String username) {
        try {
            return keeper.getUser(username) != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeUser(String username) {
        try {
            keeper.removeUser(username);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserPermission(String username, String... permissions) {
        try {
            if (!keeper.userWithNameExists(username)) {
                return false;
            }
            keeper.updatePermissions(username, new HashSet<>(Arrays.asList(permissions)));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String[] getUserPermissions(String username) {
        try {
            if (!keeper.userWithNameExists(username)) {
                return new String[0];
            }
            return keeper.getPermissions(username).toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    /**
     * Clear all the user from the system
     */
    public void burnThePlace() {
        try {
            keeper.clearFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
