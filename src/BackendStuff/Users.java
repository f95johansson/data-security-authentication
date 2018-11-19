package BackendStuff;

import Roles.Role;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Roles.Role.POWER_USER;


/**
 * Reads and updates all the users and the passwords to a file
 */
public class Users {

    private final String PASSWORD_PATH = "User-records.txt";

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
     *
     * @throws IOException could not write to password file
     */
    private void updateUser(String username, UnaryOperator<User> userChanger) throws IOException {
        StringBuilder output = new StringBuilder();
        getAllUsers()
                .map(user -> {
                    if (user.name.equals(username)) {
                        return userChanger.apply(user);
                    } else {
                        return user;
                    }
                })
                .filter(Objects::nonNull)
                .map(User::toString)
                .forEach(output::append);

        Files.write(Paths.get(PASSWORD_PATH), output.toString().getBytes(), StandardOpenOption.WRITE);
    }

    /**
     * Updates the salt and the password for the specified user
     *
     * @throws IOException could not write to password file
     */
    public void updatePassword(String username, String newSalt, String newHashPassword) throws IOException {
        updateUser(username, user -> new User(username, user.role, newSalt, newHashPassword));
    }

    public void updateUserRole(String username, Role newRole) throws IOException {
        updateUser(username, user -> new User(user.name, newRole, user.salt, user.salt));
    }

    public void removeUser(String username) throws IOException {
        updateUser(username, user -> null);
    }

    protected String[] allLinesInFile() throws IOException {
        return new String(Files.readAllBytes(Paths.get(PASSWORD_PATH))).split("\n");
    }

    private Stream<User> getAllUsers() throws IOException {
        return Arrays.stream(allLinesInFile())
                .filter(str -> !str.isEmpty())
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

    /**
     * Adds a user to the password file
     *
     * @throws IOException could not write
     */
    public void addUser(User user) throws IOException {
        String lineToAppend = user.toString();
        Files.write(Paths.get(PASSWORD_PATH), lineToAppend.getBytes(), StandardOpenOption.APPEND);
    }

    /**
     * Test to see if user with name exists
     *
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
