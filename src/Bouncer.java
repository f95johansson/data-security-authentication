/*
 * File: TheBouncer.java
 * Author: Fredrik Johansson
 * Date: 2018-11-07
 */

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Bouncer {
    private final Users users;
    private final HashMap<String, UserToken> sessionKeys;

    public Bouncer(Users user) {
        this.users = user;
        sessionKeys = new HashMap<>();
    }

    //returns null if not valid otherwise returns name
    public String validSessionKey(String sessionKey) {
        if (sessionKey == null) return null;

        UserToken sessionUserToken = sessionKeys.getOrDefault(sessionKey, null);
        if (sessionUserToken == null) {
            return null;
        } else if (sessionUserToken.expirationTime.isBefore(LocalDateTime.now())) {
            sessionKeys.remove(sessionKey);
            return null;
        } else {
            return sessionUserToken.username;
        }
    }

    public String startSession(String username, String password) {
        if (!validLogin(username, password)) return null;

        String sessionKey;
        try {
            sessionKey = generateSessionKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        sessionKeys.put(sessionKey, new UserToken(username, LocalDateTime.now().plusHours(2)));
        return sessionKey;
    }

    private static String generateSessionKey() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] key = new byte[32];
        sr.nextBytes(key);
        return Crypto.toHex(key);
    }

    private boolean validLogin(String username, String password) {
        try {
            User user = users.getUser(User.formatUserName(username));
            return user != null && user.hash.equals(Crypto.toHash(password, user.salt));
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
