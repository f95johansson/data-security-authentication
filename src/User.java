import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Holds user information
 */
public class User {

    public final String username;
    public final String salt;
    public final String hash;
    public final Set<Permissions> permissions;

    public User(String username, String salt, String hash, Set<Permissions> permissions) {
        this.username = User.formatUserName(username);
        this.salt = salt;
        this.hash = hash;
        this.permissions = permissions;
    }

    public User(String username, String salt, String hash, String[] permissions) {
        this(username, salt, hash, Arrays.stream(permissions).map(Permissions::fromString).filter(Objects::nonNull).collect(Collectors.toSet()));
    }

    public String toString() {
        return String.format("%s,%s,%s,%s", formatUserName(username), salt, hash, permissions.stream().map(p -> p.string).collect(Collectors.joining(";")));
    }

    /**
     * @return A valid username (no ",")
     */
    public static String formatUserName(String username) {
        return username.replace(",", "");
    }
}
