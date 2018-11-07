/*
 * File: User.java
 * Author: Fredrik Johansson
 * Date: 2018-11-07
 */

public class User {

    public final String username;
    public final String salt;
    public final String hash;

    public User(String username, String salt, String hash) {
        if (username.contains(",")) {
            username = username.replace(",","");
        }
        this.username = username;
        this.salt = salt;
        this.hash = hash;
    }

    public String toString() {
        return String.format("%s,%s,%s", username, salt, hash);
    }
}
