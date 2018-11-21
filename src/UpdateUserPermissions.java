import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UpdateUserPermissions {
    public static void main(String[] args) {
        UserRegistration userRegistration = new UserRegistration(new Users());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        boolean encounteredError;
        do {
            encounteredError = false;
            try {
                System.out.print("Username: ");
                String username = reader.readLine();

                System.out.print("New permissions (eg. print;queue): ");
                String permissions = reader.readLine();

                boolean succeeded = userRegistration.updateUserPermission(username, permissions.split(";"));

                if (!succeeded) {
                    System.out.println("Could not update permissions for that user");
                    encounteredError = true;
                }
            } catch(IOException e) {
                System.out.println("IO-exception, please try again");
                encounteredError = true;
            }
        } while (encounteredError);
    }
}
