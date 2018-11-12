import java.time.LocalDateTime;

public class UserToken {
    public final String username;
    public final LocalDateTime expireTime;

    public UserToken(String username, LocalDateTime expireTime) {
        this.username = username;
        this.expireTime = expireTime;
    }
}
