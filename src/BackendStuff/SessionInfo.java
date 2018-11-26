package BackendStuff;

import java.time.LocalDateTime;


/**
 * Holds a name and the expiration time for the sessions of that user
 */
public class SessionInfo {
    final String username;
    final LocalDateTime expirationTime;

    SessionInfo(String username, LocalDateTime expirationTime) {
        this.username = username;
        this.expirationTime = expirationTime;
    }
}
