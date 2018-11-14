import static Roles.Role.*;

import BackendStuff.UserRegistration;
import org.junit.Before;

public class RolesTest {

    MockUsers users;


    @Before
    public void addUsers() {
        users = new MockUsers();

        UserRegistration ur = new UserRegistration(users);

        ur.addUser("Alice", "CatsRuleDogsDroole", ADMIN);
        ur.addUser("Bob", "kittensWithMittens", MAINTAINER);
        ur.addUser("Cecilia", "eyeOfTheTiger", POWER_USER);

        ur.addUser("David", "dogPerson", USER);
        ur.addUser("Erica", "dogPerson", USER);
        ur.addUser("Fred", "dogPerson", USER);
        ur.addUser("George", "dogPerson", USER);
    }

}
