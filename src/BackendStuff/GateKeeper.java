package BackendStuff;

import Roles.Method;
import Roles.RoleCheck;

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
     *
     * @param sessionKey - The session key in question
     * @param method, the function that calls this function as an enum
     * @return null if it is not valid, the user otherwise
     */
    public User validSessionKey(String sessionKey, Method method) {
        if (sessionKey == null) return null;

        SessionInfo sessionInfo = sessionKeys.getOrDefault(sessionKey, null);
        if (sessionInfo == null) {
            return null;
        } else if (sessionInfo.expirationTime.isBefore(LocalDateTime.now())) {
            sessionKeys.remove(sessionKey);
            return null;
        }

        try {
            User user = users.getUser(sessionInfo.username);
            if (user == null) return null;
            return RoleCheck.isAllowed(user.role, method) ? user : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUser(String sessionKey) {
        SessionInfo sessionInfo = sessionKeys.getOrDefault(sessionKey, null);
        if (sessionInfo == null) return null;

        try {
            return users.getUser(sessionInfo.username);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Attempts to start a session
     *
     * @param username - The name of the user
     * @param password - The password (unhashed)
     * @return null if the attempt was not unsuccessfull, a session key otherwise
     */
    public String startSession(String username, String password) {
        User user = validLogin(username, password);
        if (user == null) return null;

        String sessionKey;
        try {
            sessionKey = generateSessionKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        sessionKeys.put(sessionKey, new SessionInfo(user.name, LocalDateTime.now().plusHours(2)));
        return sessionKey;
    }

    /**
     * Creates a nonce that can be used as a session key
     *
     * @return A random string for use as a session key
     */
    private static String generateSessionKey() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] key = new byte[32];
        sr.nextBytes(key);
        return Crypto.toHex(key);
    }

    /**
     * Checks whether the name exists and if the password provided is the correct password for the user
     *
     * @param username - The name of the user
     * @param password - The password (unhashed)
     * @return The user if the client got successfully authenticated, null otherwise
     */
    public User validLogin(String username, String password) {
        try {
            User user = users.getUser(User.formatUserName(username));

            if (user == null) return null;

            if (user.hashedPassword.equals(Crypto.toHash(password, user.salt))) {
                return user;
            }

            return null;

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
