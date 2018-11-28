import BackendStuff.Mains.*;
import BackendStuff.UserRegistration;
import BackendStuff.Users;
import ClientStuff.*;
import Interface.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.assertj.core.api.Assertions.*;

public class ScenarioFromSpecs {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private RMIPrinter printer;
    private Admin admin;
    private String adminKey;

    @Before
    public void setUp() throws IOException, NotBoundException {
        String path = temporaryFolder.newFile("test.txt").getAbsolutePath();
        Users users = new Users(path);
        UserRegistration ur = new UserRegistration(users);

        ur.addUser("Alice", "admin", "ADMIN");
        ur.addUser("Bob", "psw", "MAINTAINER");
        ur.addUser("George", "psw2", "USER");

        HelperFunctions.suppressOutput();
        Backend.startServer(8054, users);
        printer = Client.startClient(8054);
        HelperFunctions.restoreOutput();

        BackendAdmin.startServer(8055, users);
        admin = AdminClient.startAdminClient(8055);

        adminKey = admin.logInAsAdmin("Alice", "admin");
    }

    @Test
    public void scenario_from_specification() throws RemoteException {
        String bobsRole = remove_bob_and_get_his_role();
        change_georges_role_to(bobsRole);
        add_henry_with_role_user();
        add_ida_as_power_user();
    }

    private String remove_bob_and_get_his_role() throws RemoteException {
        assertThat(printer.logIn("Bob", "psw")).isNotNull();

        String role = admin.lookUpUserRole("Bob", adminKey);
        assertThat(role).isEqualTo("MAINTAINER");

        admin.removeUser("Bob", adminKey);

        assertThat(printer.logIn("Bob", "psw")).isNull();
        return role;
    }

    private void change_georges_role_to(String bobsRole) throws RemoteException {
        assertThat(printer.logIn("George", "psw2")).isNotNull();
        admin.changeUserRole("George", bobsRole, adminKey);
        //assertThat(admin.lookUpUserRole("George", adminKey)).isEqualTo(bobsRole);
        assertThat(printer.logIn("George", "psw2")).isNotNull();
    }

    private void add_ida_as_power_user() throws RemoteException {
        admin.addUser("Ida", "idasDefaultPassword", "POWER_USER", adminKey);
        assertThat(admin.lookUpUserRole("Ida", adminKey)).isEqualTo("POWER_USER");
        assertThat(printer.logIn("Ida", "idasDefaultPassword")).isNotNull();
    }

    private void add_henry_with_role_user() throws RemoteException {
        admin.addUser("Henry", "henrysDefaultPassword", "USER", adminKey);
        assertThat(admin.lookUpUserRole("Henry", adminKey));
        assertThat(printer.logIn("Henry", "henrysDefaultPassword")).isNotNull();
    }
}
