package BackendStuff;

/**
 * A job for a specific printer and a corresponding document
 */
public class Job {
    public final int jobNumber;
    public final String fileName;
    public final String printer;

    public Job(int jobNumber, String fileName, String printer) {
        this.jobNumber = jobNumber;
        this.fileName = fileName;
        this.printer = printer;
    }
}