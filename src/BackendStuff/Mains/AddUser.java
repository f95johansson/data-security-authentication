package BackendStuff.Mains;

import BackendStuff.Users;
import Roles.Role;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Script to help add a user to the current systems set of passwords
 */
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

                System.out.print("Password (clear text input): ");
                String password = reader.readLine();

                Role role = getRoleFromInput(reader);

                boolean succeeded = userRegistration.addUser(username, password, role);

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

    private static Role getRoleFromInput(BufferedReader reader) throws IOException {
        Role[] roles = Role.values();
        for (int i = 0; i < roles.length; i++) {
            System.out.println(i + ". " + roles[i].name());
        }

        System.out.println("Which role, use the number keys for selection");

        Role role = null;

        do {
            try {
                int roleNumber = Integer.parseInt(reader.readLine().trim());
                role = roles[roleNumber];

                if (role == null) {
                    System.out.println("That role does not exist, sorry");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please choose a number between 0 and " + (roles.length-1));
            }
        } while (role == null);
        return role;
    }
}
