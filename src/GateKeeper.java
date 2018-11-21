import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Responsible for the authentication of clients, keeps track of session keys and handles log in attempts
 */
public class GateKeeper {
    private final Users users;
    private final HashMap<String, SessionInfo> sessionKeys;

    public GateKeeper(Users user) {
        this.users = user;
        sessionKeys = new HashMap<>();
    }

    /**
     * Gets the name of the user that was provided that session key
     * @param sessionKey - The session key in question
     * @return null if not valid otherwise returns name
     */
    public User validSessionKey(String sessionKey) {
        if (sessionKey == null) return null;

        SessionInfo sessionInfo = sessionKeys.getOrDefault(sessionKey, null);
        if (sessionInfo == null) {
            return null;
        } else if (sessionInfo.expirationTime.isBefore(LocalDateTime.now())) {
            sessionKeys.remove(sessionKey);
            return null;
        } else {
            return sessionInfo.user;
        }
    }

    /**
     * Attempts to start a session
     * @param username - The name of the user
     * @param password - The password (unhashed)
     * @return null if the attempt was not unsuccessfull, a session key otherwise
     */
    public String startSession(String username, String password) {
        User user = validLogin(username, password);
        if (user == null) return null; // invalid login

        String sessionKey;
        try {
            sessionKey = generateSessionKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        sessionKeys.put(sessionKey, new SessionInfo(user, LocalDateTime.now().plusHours(2)));
        return sessionKey;
    }

    /**
     * Creates a nonce that can be used as a session key
     * @return A random string for use as a session key
     */
    private static String generateSessionKey() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] key = new byte[32];
        sr.nextBytes(key);
        return Crypto.toHex(key);
    }

    /**
     * Checks whether the username exists and if the password provided is the correct password for the user
     * @param username - The name of the user
     * @param password - The password (unhashed)
     * @return True if the client got successfully authenticated, false otherwise
     */
    private User validLogin(String username, String password) {
        try {
            User user = users.getUser(User.formatUserName(username));
            if (user != null && user.hash.equals(Crypto.toHash(password, user.salt))) {
                return user;
            } else {
                return null;
            }
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
