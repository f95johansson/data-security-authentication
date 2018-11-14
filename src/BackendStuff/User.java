package BackendStuff;

import Roles.Role;

/**
 * Holds user information
 */
public class User {

    public final String name;
    public final String salt;
    public final String hashedPassword;
    public final Role role;

    public User(String name, Role role, String salt, String hashedPassword) {
        this.name = User.formatUserName(name);
        this.salt = salt;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public String toString() {
        return String.format("%s,%s,%s,%s", name, role.name(), salt, hashedPassword);
    }

    /**
     * @return A valid name (no ",")
     */
    public static String formatUserName(String username) {
        return username.replace(",", "");
    }
}
