import BackendStuff.UserRegistration;
import BackendStuff.Services.AdminService;
import BackendStuff.Services.PrinterService;
import BackendStuff.Users;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;

import static Roles.Role.*;

public class RolesAvailability {

    private Users users;
    private PrinterService printerService;

    @Before
    public void startUp() throws RemoteException {
        users = new MockUsers();
        UserRegistration ur = new UserRegistration(users);

        ur.addUser("Alice", "CatsRuleDogsDrool", ADMIN);
        ur.addUser("Bob", "kittensWithMittens", MAINTAINER);
        ur.addUser("Cecilia", "eyeOfTheTiger", POWER_USER);

        ur.addUser("David", "dogPerson", USER);

        printerService = new PrinterService(users);
    }

    @Test
    public void admin_can_do_everything() throws RemoteException {
        String sKey = printerService.logIn("Alice", "CatsRuleDogsDrool");

        int job = printerService.print("", "", sKey);
        printerService.queue(sKey);
        printerService.topQueue(job, sKey);
        printerService.setConfig("hej", "0", sKey);
        printerService.readConfig("hej", sKey);
        printerService.restart(sKey);
        printerService.stop(sKey);
        printerService.start(sKey);
        printerService.status(sKey);

        AdminService as = new AdminService(users);

        String sKey2 = as.logInAsAdmin("Alice", "CatsRuleDogsDrool");
        as.addUser("hej", "hej", USER, sKey2);
        as.changeUserRole("hej", POWER_USER, sKey2);
        as.lookUpUserRole("hej", sKey2);
        as.removeUser("hej", sKey2);
    }

    @Test
    public void maintainer_can_start_stop_and_check_status() throws RemoteException {
        String sKey = printerService.logIn("Bob", "kittensWithMittens");

        printerService.start(sKey);
        printerService.stop(sKey);
        printerService.status(sKey);
        printerService.restart(sKey);
    }

    @Test
    public void power_user_can_do_user_stuff_and_cheat_the_queue_and_restart() throws RemoteException {
        String sKey = printerService.logIn("Cecilia", "eyeOfTheTiger");

        printerService.queue(sKey);
        printerService.restart(sKey);
        int job = printerService.print("", "", sKey);
        printerService.topQueue(job, sKey);
    }

    @Test
    public void user_can_print_and_see_queue() throws RemoteException {
        String sKey = printerService.logIn("David", "dogPerson");

        printerService.queue(sKey);
        printerService.print("", "", sKey);
    }
}
