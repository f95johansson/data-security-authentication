import BackendStuff.UserRegistration;
import BackendStuff.Services.AdminService;
import BackendStuff.Services.PrinterService;
import BackendStuff.Users;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;

import static Roles.Role.*;

import static org.assertj.core.api.Assertions.*;

public class RolesSecurity {

    private PrinterService printerService;
    private AdminService adminService;

    @Before
    public void startUp() throws RemoteException {
        Users users = new MockUsers();
        UserRegistration ur = new UserRegistration(users);

        ur.addUser("Bob", "kittensWithMittens", MAINTAINER);
        ur.addUser("Cecilia", "eyeOfTheTiger", POWER_USER);

        ur.addUser("David", "dogPerson", USER);

        printerService = new PrinterService(users);
        adminService = new AdminService(users);
    }

    @Test
    public void maintainer_cant_print_and_mess_with_configs() throws RemoteException {
        String sKey = printerService.logIn("Bob", "kittensWithMittens");

        assertThatThrownBy(() -> printerService.queue(sKey));
        assertThatThrownBy(() -> printerService.print("", "", sKey));
        assertThatThrownBy(() -> printerService.setConfig("", "", sKey));
        assertThatThrownBy(() -> printerService.readConfig("", sKey));

        //need there to be a print in the queue
        int job = printerService.print("", "", printerService.logIn("David", "dogPerson"));
        assertThatThrownBy(() -> printerService.topQueue(job, sKey));


        assertThat(adminService.logInAsAdmin("Bob", "kittensWithMittens")).isNull();
    }

    @Test
    public void power_user_cannot_do_start_stop_or_mess_with_config() throws RemoteException {
        String sKey = printerService.logIn("Cecilia", "eyeOfTheTiger");

        assertThatThrownBy(() -> printerService.setConfig("", "", sKey));
        assertThatThrownBy(() -> printerService.readConfig("", sKey));

        assertThatThrownBy(() -> printerService.start(sKey));
        assertThatThrownBy(() -> printerService.stop(sKey));

        assertThat(adminService.logInAsAdmin("Cecilia", "eyeOfTheTiger")).isNull();
    }

    @Test
    public void user_can_only_print_and_see_queue() throws RemoteException {
        String sKey = printerService.logIn("David", "dogPerson");

        assertThatThrownBy(() -> printerService.stop(sKey));
        assertThatThrownBy(() -> printerService.start(sKey));
        assertThatThrownBy(() -> printerService.restart(sKey));
        assertThatThrownBy(() -> printerService.status(sKey));
        assertThatThrownBy(() -> printerService.setConfig("", "", sKey));
        assertThatThrownBy(() -> printerService.readConfig("", sKey));

        int job = printerService.print("", "", sKey);
        assertThatThrownBy(() -> printerService.topQueue(job, sKey));

        assertThat(adminService.logInAsAdmin("David", "dogPerson")).isNull();
    }

}
