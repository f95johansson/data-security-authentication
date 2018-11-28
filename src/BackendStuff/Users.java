package BackendStuff;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Reads and updates the users and the passwords stored in a file
 */
public class Users {

    private static final String DEFAULT_PASSWORD_PATH = "User-records.txt";
    private final Path password_path;

    public Users() {
        this(DEFAULT_PASSWORD_PATH);
    }

    public Users(String path) {
        this(Paths.get(path));
    }

    public Users(Path path) {
        password_path = path;

        if (!Files.exists(password_path)) {
            try {
                Files.createFile(password_path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return The User with the provided name, or if name does not exists, then null
     */
    public User getUser(String username) throws IOException {
        if (username == null) return null;

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

        Files.write(password_path, output.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Updates the salt and the password for the specified user
     *
     * @throws IOException could not write to password file
     */
    public void updatePassword(String username, String newSalt, String newHashPassword) throws IOException {
        updateUser(username, user -> new User(username, user.role, newSalt, newHashPassword));
    }

    public void updateUserRole(String username, String newRole) throws IOException {
        updateUser(username, user -> new User(user.name, newRole, user.salt, user.hashedPassword));
    }

    public void removeUser(String username) throws IOException {
        updateUser(username, user -> null);
    }

    protected Stream<String> allLinesInFile() throws IOException {
        return Files.lines(password_path);
    }

    private Stream<User> getAllUsers() throws IOException {
        return allLinesInFile()
                .filter(str -> !str.isEmpty())
                .map(line -> {
                    try {
                        String[] words = line.split(",");

                        return new User(words[0], words[1], words[2], words[3]);

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
        Files.write(password_path, lineToAppend.getBytes(), StandardOpenOption.APPEND);
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
        Files.write(password_path, "".getBytes());
    }
}