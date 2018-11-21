package BackendStuff.Functionality;

import BackendStuff.Job;
import BackendStuff.Status;
import BackendStuff.StringOrException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PrinterFunctions {

    private Map<String, String> settings = new HashMap<>();

    private List<Job> jobs = new ArrayList<>();
    private Status currentStatus = Status.On;

    private int uniqueIDGenerator = 0;

    public int print(StringOrException filename, StringOrException printer) {
        Job job = new Job(uniqueIDGenerator++, filename.value, printer.value);
        jobs.add(jobs.size(), job);
        return job.jobNumber;
    }

    public String queue() {
        return jobs.stream()
                .map(job -> "<" + job.jobNumber + "> <" + job.fileName + ">")
                .reduce((a, b) -> a + "\n" + b)
                .orElse("[No jobs]");
    }

    public boolean topQueue(int job)  {
        Job toMove = jobs.stream()
                .filter(j -> j.jobNumber == job)
                .findFirst()
                .orElse(null);

        if (toMove == null) return false;
        
        jobs.remove(toMove);
        jobs.add(0, toMove);
        
        return true;
    }

    public void start()  {
        currentStatus = Status.On;
    }

    public void stop()  {
        currentStatus = Status.Off;
    }

    public void restart()  {
        stop();
        jobs.clear();
        start();
    }

    public String status()  {
        return currentStatus.name();
    }

    public String readConfig(StringOrException parameter)  {
        return settings.getOrDefault(parameter.value, null);
    }

    public void setConfig(StringOrException parameter, StringOrException value)  {
        settings.put(parameter.value, value.value);
    }
}