import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

interface RMIPrinter extends Remote {
  public void print(String filename, String printer) throws RemoteException;
  public void queue() throws RemoteException;
  public void topQueue(int job) throws RemoteException;   // moves job to the top of the queue
  public void start() throws RemoteException;   // starts the print server
  public void stop() throws RemoteException;   // stops the print server
  public void restart() throws RemoteException;   // stops the print server, clears the print queue and starts the print server again
  public void status() throws RemoteException;  // prints status of printer on the user's display
  public void readConfig(String parameter) throws RemoteException;   // prints the value of the parameter on the user's display
  public void setConfig(String parameter, String value) throws RemoteException;   // sets the parameter to value
}

class PrinterService extends UnicastRemoteObject implements RMIComputer {
  
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
  }
  
  public void queue() throws RemoteException {
    log("QUEUE");
  }
  
  public void topQueue(int job) throws RemoteException {
    log("TOP QUEUE", String.valueOf(job));
  }
  
  public void start() throws RemoteException {
    log("START");
  }
  
  public void stop() throws RemoteException {
    log("STOP");
  }
  
  public void restart() throws RemoteException {
    log("RESTART");
  }
  
  public void status() throws RemoteException {
    log("STATUS");
  }
  
  public void readConfig(String parameter) throws RemoteException {
    log("READ CONFIG", parameter);
  }
  public void setConfig(String parameter, String value) throws RemoteException {
    log("SET CONFIG", "(" + parameter + ", " + value + ")");
  }
}