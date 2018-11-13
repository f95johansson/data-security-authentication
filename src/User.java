
/**
 * Holds user information
 */
public class User {

    public final String username;
    public final String salt;
    public final String hash;

    public User(String username, String salt, String hash) {
        this.username = User.formatUserName(username);
        this.salt = salt;
        this.hash = hash;
    }

    public String toString() {
        return String.format("%s,%s,%s", username, salt, hash);
    }

    /**
     * @return A valid username (no ",")
     */
    public static String formatUserName(String username) {
        return username.replace(",", "");
    }
}
