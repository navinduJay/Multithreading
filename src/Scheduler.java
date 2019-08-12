import java.util.*;

/**
 * The scheduler keeps track of all the jobs, and runs each one at the appropriate time. (You need
 * to fill in the details!)
 */
public class Scheduler
{
    // ...
    private Thread thread;
    private ArrayList<Job> jobs = new ArrayList<>();
    private Object mutex;

    public Scheduler(Thread thread, ArrayList<Job> jobs, Object mutex) {
        this.thread = thread;
        this.jobs = jobs;
        this.mutex = mutex;
    }

    public void addJob(Job newJob)
    {
        // ...
    }
    
    public void start()
    {
        // ...
    }

    public void stop()
    {
        // ...
    }
}
