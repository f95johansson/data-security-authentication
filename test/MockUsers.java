import BackendStuff.User;
import BackendStuff.Users;
import Roles.Role;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class MockUsers extends Users {
    private Set<String> lines = new HashSet<>();

    @Override
    protected Stream<String> allLinesInFile() {
        return lines.stream();
    }
    @Override
    public void addUser(User user) {
        lines.add(user.toString().trim());
    }

    @Override
    public void updatePassword(String username, String newSalt, String newHashPassword) {
        //Not implemented
    }

    @Override
    public void clearFile() {
        lines.clear();
    }

    @Override
    public void updateUserRole(String username, Role newRole)  {
        //Not implemented
    }

    @Override
    public void removeUser(String username) {
        // Not implemented
    }
}