/*
 * File: TheKeeperOfRecords.java
 * Author: Fredrik Johansson
 * Date: 2018-11-07
 */

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

public class TheKeeperOfRecords {

    private final String PASSWORD_PATH =  "thesearenotpassword-goaway.passwords";

    public User getUser(String username) throws IOException {
        return Arrays.stream(getAllUsers()).filter(user -> user.username.equals(username)).findFirst().orElse(null);
    }

    public void updatePassword(String username, String salt, String newHashPassword) throws IOException {

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(PASSWORD_PATH))) {
            Arrays.stream(getAllUsers())
                    .map(user -> {
                        if (user.username.equals(username)) {
                            return new User(username, salt, newHashPassword);
                        } else {
                            return user;
                        }
                    }).forEach(user -> {
                        try {
                            writer.write(user.toString());
                        } catch (IOException e) {
                            System.err.println("Could not write to password file");
                        }
                    });
        }
    }

    private User[] getAllUsers() throws IOException {
        String[] content = new String(Files.readAllBytes(Paths.get(PASSWORD_PATH))).split("\n");
        return Arrays.stream(content)
                .filter(line -> !line.startsWith("#"))
                .map(line -> {
                    try {
                        String[] attrs = line.split(",");
                        return new User(attrs[0], attrs[1], attrs[2]);
                    } catch (IndexOutOfBoundsException e) {
                        System.err.println("line: " + line + " was bad");
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toArray(User[]::new);
    }
}
