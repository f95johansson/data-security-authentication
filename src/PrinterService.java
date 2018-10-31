import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

interface RMIPrinter extends Remote {
  void print(String filename, String printer) throws RemoteException;
  String queue() throws RemoteException;
  void topQueue(int job) throws RemoteException;   // moves job to the top of the queue
  void start() throws RemoteException;   // starts the print server
  void stop() throws RemoteException;   // stops the print server
  void restart() throws RemoteException;   // stops the print server, clears the print queue and starts the print server again
  String status() throws RemoteException;  // prints currentStatus of printer on the user's display
  String readConfig(String parameter) throws RemoteException;   // prints the value of the parameter on the user's display
  void setConfig(String parameter, String value) throws RemoteException;   // sets the parameter to value
}

enum Status {
  On, Off, Printing
}

class Job{
  public int jobNumber;
  public String fileName;
  public String printer;

  public Job(int jobNumber, String fileName, String printer) {
    this.jobNumber = jobNumber;
    this.fileName = fileName;
    this.printer = printer;
  }
}

public class PrinterService extends UnicastRemoteObject implements RMIPrinter {

  private Map<String, String> settings = new HashMap<>();
  private List<Job> jobs = new LinkedList<>();
  private Status currentStatus = Status.On;

  private int uniqueIDGenerator = 0;

  public PrinterService() throws RemoteException {
    super();
  }
  
  private void log(String method) {
    System.out.println("[" + method + "]");
  }
  
  private void log(String method, String description) {
    System.out.println("[" + method + "]: " + description);
  }
  
  public void print(String filename, String printer) throws RemoteException {
    log("PRINT", "(" + filename + ", " + printer + ")");
    pushPrintJob(filename, printer);
  }

  private void pushPrintJob(String filename, String printer) {
    jobs.add(jobs.size(), new Job(uniqueIDGenerator++, filename, printer));
  }
  
  public String queue() throws RemoteException {
    log("QUEUE");

    return jobs.stream()
            .map(job -> "<" + job.jobNumber + "> <" + job.fileName + ">")
            .reduce((a, b) -> a + "\n" + b)
            .orElse("[No jobs]");
  }
  
  public void topQueue(int job) throws RemoteException {
    log("TOP QUEUE", String.valueOf(job));

    Job toMove = jobs.stream()
            .filter(j -> j.jobNumber == job)
            .findFirst()
            .orElse(null);

    if (toMove == null) {
      throw new RemoteException("No job with number " + job);
    }

    jobs.remove(toMove);
    jobs.add(0, toMove);
  }
  
  public void start() throws RemoteException {
    log("START");
    currentStatus = Status.On;
  }
  
  public void stop() throws RemoteException {
    log("STOP");
    currentStatus = Status.Off;
  }
  
  public void restart() throws RemoteException {
    log("RESTART");
    stop();
    jobs.clear();
    start();
  }
  
  public String status() throws RemoteException {
    log("STATUS");
    return "Status: " + currentStatus.name();
  }
  
  public String readConfig(String parameter) throws RemoteException {
    log("READ CONFIG", parameter);
    return settings.getOrDefault(parameter, "[No such setting, use setConfig first]");
  }
  public void setConfig(String parameter, String value) throws RemoteException {
    log("SET CONFIG", "(" + parameter + ", " + value + ")");
    settings.put(parameter, value);
  }
}