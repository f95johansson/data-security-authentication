import BackendStuff.Services.AdminService;
import BackendStuff.UserRegistration;
import BackendStuff.Services.PrinterService;
import BackendStuff.Users;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.rmi.RemoteException;

import static org.assertj.core.api.Assertions.*;

public class ChangingUsers {
    private PrinterService printerService;
    private AdminService adminService;

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    private String adminKey;
    private String userKey;

    @BeforeClass
    public static void setUp() {
        HelperFunctions.suppressOutput();
    }

    private static int uniqueId = 0;

    @Before
    public void before() throws IOException {
        Users users = new Users(folder.newFile((uniqueId++) + ".txt").getAbsolutePath());
        UserRegistration userRegistration = new UserRegistration(users);
        userRegistration.addUser("admin", "admin", "ADMIN");
        userRegistration.addUser("user", "user", "USER");

        adminService = new AdminService(users);
        printerService = new PrinterService(users);

        adminKey = adminService.logInAsAdmin("admin", "admin");
        userKey = printerService.logIn("user", "user");
    }

    @Test
    public void role_changes_effects_immediately() throws RemoteException {
        printerService.print("hej", "hej", userKey);
        assertThatThrownBy(() -> printerService.restart(userKey));

        adminService.changeUserRole("user", "MAINTAINER", adminKey);

        assertThatThrownBy(() -> printerService.print("hej", "hej", userKey));
        printerService.restart(userKey);
    }

    @Test
    public void removal_effects_immediately() throws RemoteException {
        printerService.print("hej", "hej", userKey);

        adminService.removeUser("user", adminKey);

        assertThatThrownBy(() -> printerService.print("hej2", "hej", userKey));
    }

    @Test
    public void add_effects_immediately() throws RemoteException {
        assertThat(printerService.logIn("data", "security")).isNull();
        adminService.addUser("data", "security", "USER", adminKey);
        assertThat(printerService.logIn("data", "security")).isNotNull();
    }

    @Test
    public void changePassword() throws RemoteException {
        adminService.addUser("data", "security", "USER", adminKey);
        printerService.changeMyPassword("data", "security", "confidentiality");
        assertThat(printerService.logIn("data", "confidentiality")).isNotNull();
    }

    @Test
    public void migrateRole() throws RemoteException {
        adminService.addUser("Bob", "DogsAreOkay", "MAINTAINER", adminKey);
        String oldRole = adminService.lookUpUserRole("Bob", adminKey);
        adminService.removeUser("Susan", adminKey);
        adminService.addUser("Susan", "xf124ss", oldRole, adminKey);
        assertThat(printerService.changeMyPassword("Susan", "xf124ss", "CatsAreHats")).isTrue();
        assertThat(printerService.logIn("Bob", "psw")).isNull();
        assertThat(printerService.logIn("Susan", "CatsAreHats"));
    }
}
