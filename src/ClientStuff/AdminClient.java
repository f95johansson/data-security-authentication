package ClientStuff;

import Interface.Admin;
import Interface.RMIPrinter;
import Roles.Role;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Connects a client to a admin server
 */
public class AdminClient {

    private static Admin admin;
    private static String sessionKey;

    /**
     * Connect to a admin server (on localhost:8099)
     *
     * @return the connected client
     */
    private static Admin startAdminClient() throws RemoteException, NotBoundException, MalformedURLException {
        return startAdminClient(8098);
    }

    /**
     * Connect to a admin server on localhost using the specified port
     *
     * @return the connected client
     */
    public static Admin startAdminClient(int port) throws RemoteException, NotBoundException, MalformedURLException {
        return (Admin) Naming.lookup("rmi://localhost:" + port + "/admin");
    }

    /**
     * A test main which connects to a admin (localhost:8099) with
     * a test name and password, and performs a print action.
     * Will only work if the specified user and password exists on server
     */
    public static void main(String[] args) throws IOException, NotBoundException {
        admin = startAdminClient();

        int exitCode = -1;

        sessionKey = null;
        String username;
        String password;

        while(sessionKey == null) {
            username = getLine("username: ");
            password = getLine("password: ");
            sessionKey = admin.logInAsAdmin(username, password);

            if (sessionKey == null) {
                System.out.println("Either username or password is incorrect (or you are not an admin)");
            }
        }

        while (exitCode == -1) {
            try {
                exitCode = runInterface();
            } catch (RemoteException e) {
                System.out.println(e.getLocalizedMessage() + "\n");
            }
        }
    }

    private static int runInterface() throws RemoteException {
        int exitCode = -1;
        while (exitCode == -1) {
            try {
                System.out.println(
                        "\taddUser(username, password, role)" +
                        "\tremoveUser(username)" +
                        "\tchangeUserRole(username, newRole)"
                    );

                String line = getLine("Write function: ").trim().toLowerCase();

                if (line.equals("quit") || line.equals("exit")) {
                    exitCode = 0;
                } else {
                    final String name = "([a-z0-9_åäöÅÄÖ]+)";

                    final Pattern threeParams = Pattern.compile(name + "\\(" + name + ",[ ]?" + name + ",[ ]?" + name + "\\)");
                    final Pattern twoParams = Pattern.compile(name + "\\(" + name + ",[ ]?" + name + "\\)");
                    final Pattern oneParam = Pattern.compile(name + "\\("+ name + "\\)");

                    Matcher matches = threeParams.matcher(line);

                    if (matches.matches() && matches.groupCount() == 4) {
                        run(matches.group(1), matches.group(2), matches.group(3), matches.group(4));
                    } else {
                        matches = twoParams.matcher(line);
                        if (matches.matches() && matches.groupCount() == 3) {
                            run(matches.group(1), matches.group(2), matches.group(3));
                        } else {
                            matches = oneParam.matcher(line);
                            if (matches.matches() && matches.groupCount() == 2) {
                                run(matches.group(1), matches.group(2));
                            } else {
                                System.out.println("Did not match anything");
                            }
                        }
                    }

                    while (matches.find()) {
                        System.out.println(matches.group());
                    }
                }


            } catch(RemoteException e){
                throw e;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return exitCode;
    }

    private static void run(String name, String arg1, String arg2, String arg3) throws RemoteException {
        if (name.equals("adduser")) {
            Role role = Role.fromString(arg3.toUpperCase());

            if (role == null) {
                System.out.println("The role " + arg3 + " does not exist");
                return;
            }

            admin.addUser(arg1, arg2, role, sessionKey);
            System.out.println("Added user with role " + role);
            return;
        }

        System.out.println("There is no function " + name + " that takes two parameter '" + arg1 + "', and '" + arg2 + "'");
    }

    private static void run(String name, String arg1, String arg2) throws RemoteException {
        if (name.equals("changeuserrole")) {
            Role role = Role.fromString(arg2.toUpperCase());
            admin.changeUserRole(arg1, role, sessionKey);
            System.out.println("Changed user role");
            return;
        }
        System.out.println("There is no function " + name + " that takes two parameter '" + arg1 + "', and '" + arg2 + "'");
    }

    private static void run(String name, String arg1) throws RemoteException {
        if (name.equals("removeuser")) {
            try {
                admin.removeUser(arg1, sessionKey);
                System.out.println("Removed user");
            } catch (NumberFormatException ex) {
                System.err.println("Could not convert " + arg1 + " to int");
            }

            return;
        }

        System.out.println("There is no function " + name + " that takes one parameter " + arg1);
    }

    private static String getLine(String print) throws IOException {
        System.out.print(print);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }
} 