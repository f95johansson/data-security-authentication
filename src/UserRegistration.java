import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

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

    public boolean updateUserPermission(String username, String... permissions) {
        try {
            if (!keeper.userWithNameExists(username)) {
                return false;
            }
            keeper.updatePermissions(username, Arrays.stream(permissions).map(Permissions::fromString).collect(Collectors.toSet()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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
