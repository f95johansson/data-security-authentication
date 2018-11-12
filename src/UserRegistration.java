import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class UserRegistration {
    private Users keeper;

    public UserRegistration(Users keeper) {
        this.keeper = keeper;
    }

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
            keeper.addUser(new User(username, salt, hashedPassword));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void burnThePlace() {
        try {
            keeper.clearFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
