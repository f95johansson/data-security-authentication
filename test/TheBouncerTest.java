/*
 * File: TheBouncerTest.java
 * Author: Fredrik Johansson
 * Date: 2018-11-07
 */

import org.junit.Test;

import static org.junit.Assert.*;

public class TheBouncerTest {


    @Test
    public void shouldLogMeIn() {
        TheBouncer bouncer = new TheBouncer();
        assertNotNull(bouncer.enterClub("usernameusername", "password123"));
    }


    @Test
    public void shouldNotLogInInvalidPassword() {
        TheBouncer bouncer = new TheBouncer();
        assertNull(bouncer.enterClub("usernameusername", "password1234"));
    }

    @Test
    public void shouldNotLogInInvalidUser() {
        TheBouncer bouncer = new TheBouncer();
        assertNull(bouncer.enterClub("usernameusernameusername", "password123"));
    }

}