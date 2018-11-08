/*
 * File: TheBouncerTest.java
 * Author: Fredrik Johnson
 * Date: 2018-11-07
 */

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class TheBouncerTest {

    private TheBouncer bouncer;
    private MockRecords mockRecords;
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "psw123";

    private class MockRecords extends TheKeeperOfRecords {
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
        TheIdProvider ids = new TheIdProvider(mockRecords, new TheHasher());
        ids.addUser(username, password);
    }

    @Before
    public void before() {
        mockRecords = new MockRecords();
        bouncer = new TheBouncer(mockRecords, new TheHasher());
    }

    @Test
    public void shouldLogMeIn() {
        addUser(USERNAME, PASSWORD);
        assertNotNull(bouncer.enterClub(USERNAME, PASSWORD));
    }

    @Test
    public void shouldNotLogInInvalidPassword() {
        addUser(USERNAME, PASSWORD);
        assertNull(bouncer.enterClub(USERNAME, PASSWORD + "4"));
    }

    @Test
    public void shouldNotLogInInvalidUser() {
        addUser(USERNAME, PASSWORD);
        assertNull(bouncer.enterClub(USERNAME + "hello", PASSWORD));
    }
}