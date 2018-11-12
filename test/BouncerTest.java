/*
 * File: BouncerTest.java
 * Author: Fredrik Johnson
 * Date: 2018-11-07
 */

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BouncerTest {

    private Bouncer bouncer;
    private MockUsers mockUsers;
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "psw123";

    private void addUser(String username, String password) {
        UserRegistration ids = new UserRegistration(mockUsers);
        ids.addUser(username, password);
    }

    @Before
    public void before() {
        mockUsers = new MockUsers();
        bouncer = new Bouncer(mockUsers);
    }

    @Test
    public void shoudLogIn() {
        addUser(USERNAME, PASSWORD);
        assertNotNull(bouncer.startSession(USERNAME, PASSWORD));
    }

    @Test
    public void shouldNotLogInInvalidPassword() {
        addUser(USERNAME, PASSWORD);
        assertNull(bouncer.startSession(USERNAME, PASSWORD + "4"));
    }

    @Test
    public void shouldNotLogInInvalidUser() {
        addUser(USERNAME, PASSWORD);
        assertNull(bouncer.startSession(USERNAME + "hello", PASSWORD));
    }

    @Test
    public void sessionKeyShouldWorkManyTime() {
        addUser(USERNAME, PASSWORD);
        String sessionKey = bouncer.startSession(USERNAME, PASSWORD);
        assertNotNull(bouncer.validSessionKey(sessionKey));
        assertNotNull(bouncer.validSessionKey(sessionKey));
    }
}