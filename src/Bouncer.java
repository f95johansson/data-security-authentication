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
import java.util.Set;

public class Bouncer {
    private final Users users;
    private final HashMap<String, LocalDateTime> sessionKeys;
    private final HashMap<String, LocalDateTime> oneTimeKeys;

    public Bouncer(Users user) {
        this.users = user;
        sessionKeys = new HashMap<>();
        oneTimeKeys = new HashMap<>();
    }

    public boolean validKey(String key) {
        if (key == null) return false;

        LocalDateTime oneTimeKeyExpiration = oneTimeKeys.getOrDefault(key, null);
        if (oneTimeKeyExpiration != null) {
            oneTimeKeys.remove(key);
            return oneTimeKeyExpiration.isAfter(LocalDateTime.now());
        }

        LocalDateTime expireDate = sessionKeys.getOrDefault(key, null);
        return expireDate != null && expireDate.isAfter(LocalDateTime.now());
    }

    public String startSession(String username, String password) {
        if (!validLogin(username, password)) return null;

        String sessionKey;
        try {
            sessionKey = getNonce();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        sessionKeys.put(sessionKey, LocalDateTime.now().plusHours(2));
        return sessionKey;
    }

    public String oneTimeKey(String username, String password) {
        if (!validLogin(username, password)) return null;

        String sessionKey;
        try {
            sessionKey = getNonce();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        oneTimeKeys.put(sessionKey, LocalDateTime.now().plusMinutes(30));
        return sessionKey;
    }

    private static String getNonce() throws NoSuchAlgorithmException {
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
