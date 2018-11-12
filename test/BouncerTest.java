/*
 * File: BouncerTest.java
 * Author: Fredrik Johnson
 * Date: 2018-11-07
 */

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class BouncerTest {

    private Bouncer bouncer;
    private MockRecords mockRecords;
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "psw123";

    private class MockRecords extends Users {
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

    private void addUser(String username, String password) {
        UserRegistration ids = new UserRegistration(mockRecords);
        ids.addUser(username, password);
    }

    @Before
    public void before() {
        mockRecords = new MockRecords();
        bouncer = new Bouncer(mockRecords);
    }

    @Test
    public void shouldLogMeIn() {
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
    public void oneTimeLogInShouldWorkOnlyOneTime() {
        addUser(USERNAME, PASSWORD);
        String oneTimeKey = bouncer.oneTimeKey(USERNAME, PASSWORD);
        assertTrue(bouncer.validKey(oneTimeKey));
        assertFalse(bouncer.validKey(oneTimeKey));
    }

    @Test
    public void sessionKeyShouldWorkManyTime() {
        addUser(USERNAME, PASSWORD);
        String sessionKey = bouncer.startSession(USERNAME, PASSWORD);
        assertTrue(bouncer.validKey(sessionKey));
        assertTrue(bouncer.validKey(sessionKey));
    }
}