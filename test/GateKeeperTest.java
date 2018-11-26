import BackendStuff.GateKeeper;
import BackendStuff.UserRegistration;
import Roles.Method;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GateKeeperTest {

    private GateKeeper gateKeeper;
    private MockUsers mockUsers;
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "psw123";

    private void addAdmin(String username, String password) {
        UserRegistration ids = new UserRegistration(mockUsers);
        ids.addUser(username, password, "ADMIN");
    }

    @Before
    public void before() {
        mockUsers = new MockUsers();
        gateKeeper = new GateKeeper(mockUsers);
    }

    @Test
    public void shouldLogIn() {
        addAdmin(USERNAME, PASSWORD);
        assertNotNull(gateKeeper.startSession(USERNAME, PASSWORD));
    }

    @Test
    public void shouldNotLogInInvalidPassword() {
        addAdmin(USERNAME, PASSWORD);
        assertNull(gateKeeper.startSession(USERNAME, PASSWORD + "4"));
    }

    @Test
    public void shouldNotLogInInvalidUser() {
        addAdmin(USERNAME, PASSWORD);
        assertNull(gateKeeper.startSession(USERNAME + "hello", PASSWORD));
    }

    @Test
    public void sessionKeyShouldWorkManyTime() {
        addAdmin(USERNAME, PASSWORD);
        String sessionKey = gateKeeper.startSession(USERNAME, PASSWORD);
        assertNotNull(gateKeeper.validSessionKey(sessionKey, Method.PRINT));
        assertNotNull(gateKeeper.validSessionKey(sessionKey, Method.PRINT));
    }
}