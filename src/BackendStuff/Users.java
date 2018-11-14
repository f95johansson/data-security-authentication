package BackendStuff;

import Roles.Role;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * Reads and updates all the users and the passwords to a file
 */
public class Users {

    private final String PASSWORD_PATH =  "Passwords.txt";

    /**
     * @return The BackendStuff.User with the provided name, or if name does not exists, then null
     */
    public User getUser(String username) throws IOException {
        return getAllUsers()
                .filter(user -> user.name.equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Update the salt and password for the specified user
     * @throws IOException could not write to password file
     */
    public void updatePassword(String username, String salt, String newHashPassword) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(PASSWORD_PATH))) {
            getAllUsers()
                    .map(user -> {
                        if (user.name.equals(username)) {
                            return new User(username, user.role, salt, newHashPassword);
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

    protected String[] passwordLines() throws IOException {
        return new String(Files.readAllBytes(Paths.get(PASSWORD_PATH))).split("\n");
    }

    private Stream<User> getAllUsers() throws IOException {
        return Arrays.stream(passwordLines())
                .filter(String::isEmpty)
                .map(line -> {
                    try {
                        String[] words = line.split(",");
                        Role role = Role.fromString(words[1]);
                        if (role == null) throw new NullPointerException();
                        return new User(words[0], role, words[2], words[3]);

                    } catch (IndexOutOfBoundsException e) {
                        System.err.println("line: " + line + " was incorrectly formatted");
                        return null;
                    } catch (NullPointerException e) {
                        System.err.println("line: " + line + "had an incorrect role in it");
                        return null;
                    }
                })
                .filter(Objects::nonNull);
    }

    public void changeRole(String username, Role role, String salt, String hashedPassword) {

    }

    /**
     * Add user to password file
     * @throws IOException could not write
     */
    public void addUser(User user) throws IOException {
        String lineToAppend = user.toString() + "\n";
        Files.write(Paths.get(PASSWORD_PATH), lineToAppend.getBytes(), StandardOpenOption.APPEND);
    }

    /**
     * Test to see if user with name exists
     * @throws IOException could not read password file
     */
    public boolean userWithNameExists(String username) throws IOException {
        return getAllUsers()
                .anyMatch(user -> user.name.equals(username));
    }

    /**
     * Clear the password file
     */
    public void clearFile() throws IOException {
        Files.write(Paths.get(PASSWORD_PATH), "".getBytes());
    }
}
