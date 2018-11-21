import BackendStuff.User;
import BackendStuff.Users;

import java.util.HashSet;
import java.util.Set;

public class MockUsers extends Users {
    private Set<String> lines = new HashSet<>();

    @Override
    protected String[] allLinesInFile() {
        return lines.toArray(new String[0]);
    }
    @Override
    public void addUser(User user) {
        lines.add(user.toString().trim());
    }

    @Override
    public void updatePassword(String username, String newSalt, String newHashPassword)
    {
        return; //Not implemented
    }

    @Override
    public void clearFile() {
        lines.clear();
    }
}