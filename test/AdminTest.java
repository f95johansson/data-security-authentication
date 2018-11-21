import BackendStuff.Mains.BackendAdmin;
import BackendStuff.Mains.UserRegistration;
import BackendStuff.Users;
import ClientStuff.AdminClient;
import Interface.Admin;
import static Roles.Role.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Comparator;

public class AdminTest {
    private static int uniqueGenerator = 0;
    private Admin client;
    private String sessionKey;

    private static String tempDirName = "./temp";
    private static Path tempDirectory = Paths.get(tempDirName);

    @BeforeClass
    public static void CreateTempDirectory() throws IOException {
        if (!Files.exists(tempDirectory)) {
            Files.createDirectory(tempDirectory);
        } else {
            tempDirName += "p";
            tempDirectory = Paths.get(tempDirName);
            CreateTempDirectory();
        }

        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {}
        }));
    }

    @Before
    public void SetUp() throws IOException, NotBoundException {
        int val = uniqueGenerator++;
        int port = 8000 + val;

        Users users = new Users(tempDirName + "/howDidYouFindMe" + val + ".txt");
        UserRegistration ur = new UserRegistration(users);
        ur.addUser("admin", "admin", ADMIN);

        BackendAdmin.startServer(port, users);
        client = AdminClient.startAdminClient(port);
        sessionKey = client.logInAsAdmin("admin", "admin");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored") // not actually ignored
    @AfterClass
    public static void DeleteTheFolder() throws IOException {
        Files.walk(tempDirectory)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);


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
