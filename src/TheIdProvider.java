import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class TheIdProvider {
    private TheKeeperOfRecords keeper;
    private TheHasher hasher;

    public TheIdProvider(TheKeeperOfRecords keeper, TheHasher hasher) {
        this.keeper = keeper;
        this.hasher = hasher;
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
            salt = hasher.generateSalt();
            hashedPassword = hasher.toHash(password, salt);
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
