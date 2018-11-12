import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AddUser {

    public static void main(String[] args) {
        UserRegistration userRegistration = new UserRegistration(new Users());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        boolean encounteredError;
        do {
            encounteredError = false;
            try {
                System.out.print("Username: ");
                String username = reader.readLine();

                System.out.print("Password: ");
                String password = reader.readLine();

                boolean succeeded = userRegistration.addUser(username, password);

                if (!succeeded) {
                    System.out.println("That name is taken");
                    encounteredError = true;
                }
            } catch(IOException e) {
                System.out.println("IO-exception, please try again");
                encounteredError = true;
            }
        } while (encounteredError);
    }
}
