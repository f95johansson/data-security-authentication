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

public class TheBouncer {

    private final TheKeeperOfRecords keeper;
    private final TheHasher hasher;
    private HashMap<String, LocalDateTime> clubPasses;

    public TheBouncer() {
        keeper = new TheKeeperOfRecords();
        hasher = new TheHasher();
        clubPasses = new HashMap<>();
    }

    public String enterClub(String username, String password) {
        if (!checkID(username, password)) return null;

        String sessionKey = null;
        try {
            sessionKey = getNewSessionKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        clubPasses.put(sessionKey, LocalDateTime.now().plusHours(2));
        return sessionKey;
    }

    private String getNewSessionKey() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] key = new byte[32];
        sr.nextBytes(key);
        return TheHasher.toHex(key);
    }

    public boolean checkID(String username, String password) {
        try {
            User user = keeper.getUser(username);
            if (user != null) {
                return user.hash.equals(hasher.toHash(password, user.salt));
            } else {
                return false;
            }
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
