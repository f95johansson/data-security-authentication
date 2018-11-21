package BackendStuff;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class Crypto {
    private static final int ITERATIONS = 1000;

    //this is a poor mans implementation of a secret salt
    private static final String SECRET_SALT = "sweden <3 france";

    private Crypto() {}

    /**
     * Hashes a password using a one-way hashedPassword function
     * @param password The password (unhashed)
     * @param salt The extra information making identical information unique hashes
     * @return The hashed version of the password
     */
    public static String toHash(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] chars = password.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(chars, fromHex(salt + SECRET_SALT), ITERATIONS, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return toHex(hash);
    }

    /**
     * Method for generating a salt using secury random
     * @return The salt
     */
    public static String generateSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return toHex(salt);
    }

    /**
     * Converts from byte array to hex, the inverse of fromHex
     * @param byteArray the information to convert to hex
     * @return The information in hexadecimal form
     */
    public static String toHex(byte[] byteArray)  {
        BigInteger bi = new BigInteger(1, byteArray);
        String hex = bi.toString(16);
        int paddingLength = (byteArray.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    /**
     * Converts from a hexadecimal string to byte array, the inverse of toHex
     * @param hex - The hexadecimal string to convert to a byte array
     * @return the byte array representation of the hex
     */
    public static byte[] fromHex(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }
}
