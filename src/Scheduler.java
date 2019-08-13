import java.util.*;

public class Scheduler
{
    private List<Job> jobs = new ArrayList<>();
    private Timer timer = null;
    private long counter = 0;
    private Object monitor = new Object();


    // Adds a new job to the list
    public void addJob(Job newJob)
    {
        synchronized (monitor) {
            jobs.add(newJob);
        }
    }


    public void start()
    {
        if(timer != null)
        {
            // Just a sanity check.
            throw new IllegalStateException("Scheduler already started");
        }


        TimerTask schedulerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                synchronized(monitor)
                {
                    for(Job job : jobs)
                    {
                        if(counter % job.getDelay() == 0)
                        {
                            job.run();
                        }
                    }
                    counter++;
                }
            }
        };

        timer = new Timer("scheduler");

        // Run every second (1000L), starting immediately (0L).
        timer.scheduleAtFixedRate(schedulerTask, 0L, 1000L);
    }

    public void stop()
    {
        if(timer == null)
        {
            // Just a sanity check.
            throw new IllegalStateException("Scheduler already stopped");
        }

        // Stop the timer, and set it to null (so we can perform the sanity checks again).
        timer.cancel();
        timer = null;
    }
}
