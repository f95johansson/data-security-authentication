import java.time.LocalDateTime;


/**
 * Holds a username and the expiration time for the sessions of that user
 */
public class SessionInfo {
    public final String username;
    public final LocalDateTime expirationTime;

    public SessionInfo(String username, LocalDateTime expirationTime) {
        this.username = username;
        this.expirationTime = expirationTime;
    }
}
