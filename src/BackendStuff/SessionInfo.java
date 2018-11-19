package BackendStuff;

import java.time.LocalDateTime;


/**
 * Holds a name and the expiration time for the sessions of that user
 */
public class SessionInfo {
    final User user;
    final LocalDateTime expirationTime;

    SessionInfo(User user, LocalDateTime expirationTime) {
        this.user = user;
        this.expirationTime = expirationTime;
    }
}
