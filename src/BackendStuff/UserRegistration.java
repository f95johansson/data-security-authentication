package BackendStuff;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Provides registering a new user functionality
 */
public class UserRegistration {
    private Users users;

    public UserRegistration(Users users) {
        this.users = users;
    }

    /**
     * Register a new user
     * @return True if successfull, false if failed or user already exists
     */
    public boolean addUser(String username, String password, String role) {
        try {
            if (users.userWithNameExists(username)) return false;
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
            users.addUser(new User(username, role, salt, hashedPassword));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Clear all the user from the system
     */
    public void burnThePlace() {
        try {
            users.clearFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
