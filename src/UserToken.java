import java.time.LocalDateTime;

public class UserToken {
    public final String username;
    public final LocalDateTime expirationTime;

    public UserToken(String username, LocalDateTime expirationTime) {
        this.username = username;
        this.expirationTime = expirationTime;
    }
}
