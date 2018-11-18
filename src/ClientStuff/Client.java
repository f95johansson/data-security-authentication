package ClientStuff;

import Interface.RMIPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Connects a client to a printer server
 */
public class Client {

    private static RMIPrinter printer;
    private static String sessionKey;

    /**
     * Connect to a printer server (on localhost:8099)
     *
     * @return the connected client
     */
    public static RMIPrinter startClient() throws RemoteException, NotBoundException, MalformedURLException {
        return startClient(8099);
    }

    /**
     * Connect to a printer server on localhost using the specified port
     *
     * @return the connected client
     */
    public static RMIPrinter startClient(int port) throws RemoteException, NotBoundException, MalformedURLException {
        return (RMIPrinter) Naming.lookup("rmi://localhost:" + port + "/printer");
    }

    /**
     * A test main which connects to a printer (localhost:8099) with
     * a test name and password, and performs a print action.
     * Will only work if the specified user and password exists on server
     */
    public static void main(String[] args) throws IOException, NotBoundException {
        printer = startClient();

        int exitCode = -1;

        sessionKey = null;
        String username;
        String password;

        while(sessionKey == null) {
            username = getLine("username: ");
            password = getLine("password: ");
            sessionKey = printer.logIn(username, password);

            if (sessionKey == null) {
                System.out.println("Either username or password is incorrect");
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
                        "\tprint(String filename, String printer) \n" +
                        "\tString queue(String sessionKey) \n" +
                        "\tvoid topQueue(int job)\n" +
                        "\tvoid start(String sessionKey)\n" +
                        "\tvoid stop(String sessionKey)\n" +
                        "\tvoid restart(String sessionKey)\n" +
                        "\tString status(String sessionKey)\n" +
                        "\tString readConfig(String parameter)\n" +
                        "\tvoid setConfig(String parameter, String value)");

                String line = getLine("Write function: ").trim().toLowerCase();

                if (line.equals("quit") || line.equals("exit")) {
                    exitCode = 0;
                } else {
                    final Pattern twoParams = Pattern.compile("([a-z0-9åäöÅÄÖ]+)\\(([a-z0-9åäöÅÄÖ]+),[ ]?([a-z0-9åäöÅÄÖ]+)\\)");
                    final Pattern oneParam = Pattern.compile("([a-z0-9åäöÅÄÖ]+)\\(([a-z0-9åäöÅÄÖ]+)\\)");
                    final Pattern zeroParam = Pattern.compile("([a-z0-9åäöÅÄÖ]+)\\(\\)");

                    Matcher matches = twoParams.matcher(line);

                    if (matches.matches() && matches.groupCount() == 3) {
                        run(matches.group(1), matches.group(2), matches.group(3));
                    } else {
                        matches = oneParam.matcher(line);
                        if (matches.matches() && matches.groupCount() == 2) {
                            run(matches.group(1), matches.group(2));
                        } else {
                            matches = zeroParam.matcher(line);
                            if (matches.matches() && matches.groupCount() == 1) {
                                run(matches.group(1));
                            } else {
                                run(line);
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

    private static void run(String name, String arg1, String arg2) throws RemoteException {
        if (name.equals("print")) {
            System.out.println(printer.print(arg1, arg2, sessionKey));
            return;
        }

        if (name.equals("setconfig")) {
            printer.setConfig(arg1, arg2, sessionKey);
            return;
        }

        System.out.println("There is no function " + name + " that takes two parameter '" + arg1 + "', and '" + arg2 + "'");
    }

    private static void run(String name, String arg1) throws RemoteException {

        if (name.equals("topqueue")) {
            try {
                int job = Integer.parseInt(arg1);
                printer.topQueue(job, sessionKey);
            } catch (NumberFormatException ex) {
                System.err.println("Could not convert " + arg1 + " to int");
            }

            return;
        }

        if (name.equals("readconfig")) {
            System.out.println(printer.readConfig(arg1, sessionKey));
            return;
        }

        System.out.println("There is no function " + name + " that takes one parameter " + arg1);
    }

    private static void run(String name) throws RemoteException {
        if (name.equals("status")) {
            System.out.println(printer.status(sessionKey));
            return;
        }

        if (name.equals("stop")) {
            printer.stop(sessionKey);
            return;
        }

        if (name.equals("start")) {
            printer.start(sessionKey);
            return;
        }

        if (name.equals("restart")) {
            printer.restart(sessionKey);
            return;
        }

        if (name.equals("queue")) {
            System.out.println(printer.queue(sessionKey));
            return;
        }

        System.out.println("There is no parameterless function with the name '" + name + "'");
    }

    private static String getLine(String print) throws IOException {
        System.out.print(print);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }
} 