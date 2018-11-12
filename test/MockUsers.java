import java.util.HashSet;
import java.util.Set;

public class MockUsers extends Users {
    Set<String> lines = new HashSet<>();

    @Override
    protected String[] passwordLines() {
        return lines.toArray(new String[0]);
    }
    @Override
    public void addUser(User user) {
        lines.add(user.toString());
    }
}