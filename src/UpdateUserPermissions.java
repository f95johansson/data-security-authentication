import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UpdateUserPermissions {
    public static void main(String[] args) {
        UserRegistration userRegistration = new UserRegistration(new Users());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                boolean succeeded = false;

                System.out.print("Task (q = quit, u = update permission, p = print permission):\n");
                String task = reader.readLine();
                if (task.equals("q")) return;
                else if (task.equals("u")) succeeded = updatePermission(userRegistration, reader);
                else if (task.equals("p")) succeeded = printPermission(userRegistration, reader);
                else {
                    System.out.println("Invalid command");
                }

                if (!succeeded) {
                    System.out.println("Not successful, please try again");
                }
            } catch(IOException e) {
                System.out.println("IO-exception, please try again");
            }
        }
    }

    private static boolean updatePermission(UserRegistration registration, BufferedReader reader) throws IOException {
        System.out.print("Username: ");
        String username = reader.readLine();

        System.out.print("New permissions (eg. print;queue): ");
        String permissions = reader.readLine();

        return registration.updateUserPermission(username, permissions.split(";"));
    }

    private static boolean printPermission(UserRegistration registration, BufferedReader reader) throws IOException {
        System.out.print("Username: ");
        String username = reader.readLine();
        System.out.println(String.join(";", registration.getUserPermissions(username)));
        return true;
    }
}
