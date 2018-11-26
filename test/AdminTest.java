import BackendStuff.Mains.BackendAdmin;
import BackendStuff.UserRegistration;
import BackendStuff.Users;
import ClientStuff.AdminClient;
import Interface.Admin;
import static Roles.Role.*;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class AdminTest {
    private static int unique = 0;

    private Admin client;
    private String sessionKey;

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    @BeforeClass
    public static void CreateTempDirectory() {
        HelperFunctions.suppressOutput();
    }

    @Before
    public void setUp() throws IOException, NotBoundException {
        int val = unique++;
        int port = 8000 + val;

        Users users = new Users(folder.newFile(val + ".txt").getAbsolutePath());
        UserRegistration ur = new UserRegistration(users);
        ur.addUser("admin", "admin", ADMIN);

        BackendAdmin.startServer(port, users);
        client = AdminClient.startAdminClient(port);
        sessionKey = client.logInAsAdmin("admin", "admin");
    }

    @Test
    public void can_add_one_user() throws RemoteException {
        String name = "oskar";
        client.addUser(name, "whatever", ADMIN, sessionKey);
    }

    @Test(expected = RemoteException.class)
    public void cannot_add_two_users_with_same_name() throws RemoteException {
        String name = "oskar";
        client.addUser(name, "whatever", ADMIN, sessionKey);
        client.addUser(name, "whatever_else", ADMIN, sessionKey);
    }

    @Test(expected = RemoteException.class)
    public void cannot_use_null() throws RemoteException {
        client.addUser(null, "legit", ADMIN, sessionKey);
    }

    @Test(expected = RemoteException.class)
    public void cannot_use_null_as_username() throws RemoteException {
        client.addUser(null, "legit", ADMIN, sessionKey);
    }

    @Test(expected = RemoteException.class)
    public void cannot_use_null_as_password() throws RemoteException {
        client.addUser("legit", null, ADMIN, sessionKey);
    }

    @Test(expected = RemoteException.class)
    public void cannot_use_null_as_role() throws RemoteException {
        client.addUser("legit", "legit2", null, sessionKey);
    }

    @Test(expected = RemoteException.class)
    public void cannot_use_null_as_role_when_changing_role() throws RemoteException {
        client.changeUserRole("legit", null, sessionKey);
    }

    @Test(expected = RemoteException.class)
    public void cannot_use_null_as_username_when_changing_role() throws RemoteException {
        client.changeUserRole(null, POWER_USER, sessionKey);
    }

    @Test(expected = RemoteException.class)
    public void cannot_use_null_as_username_when_removing_users() throws RemoteException {
        client.removeUser(null, sessionKey);
    }
}
