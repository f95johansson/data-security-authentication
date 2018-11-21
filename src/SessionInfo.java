import java.time.LocalDateTime;


/**
 * Holds a username and the expiration time for the sessions of that user
 */
public class SessionInfo {
    public final User user;
    public final LocalDateTime expirationTime;

    public SessionInfo(User user, LocalDateTime expirationTime) {
        this.user = user;
        this.expirationTime = expirationTime;
    }
}
