package BackendStuff;

/**
 * Holds user information
 */
public class User {

    public final String name;
    public final String salt;
    public final String hashedPassword;
    public final String role;

    public User(String name, String role, String salt, String hashedPassword) {
        this.name = User.formatUserName(name);
        this.salt = salt;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public String toString() {
        return String.format("%s,%s,%s,%s\n", name, role, salt, hashedPassword);
    }

    /**
     * @return A valid name (no ",")
     */
    public static String formatUserName(String username) {
        return username.replace(",", "");
    }

    public User withRole(String role) {
        return new User(name, role, salt, hashedPassword);
    }
}
