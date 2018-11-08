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

    public TheBouncer(TheKeeperOfRecords keeper, TheHasher hasher) {
        this.keeper = keeper;
        this.hasher = hasher;
        clubPasses = new HashMap<>();
    }

    public boolean validClubStamp(String sessionKey) {
        LocalDateTime expireDate = clubPasses.getOrDefault(sessionKey, null);
        return expireDate != null && expireDate.isAfter(LocalDateTime.now());
    }

    public String enterClub(String username, String password) {
        if (!checkID(username, password)) return null;

        String sessionKey;
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

    private boolean checkID(String username, String password) {
        try {
            User user = keeper.getUser(User.formatUserName(username));
            return user != null && user.hash.equals(hasher.toHash(password, user.salt));
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
