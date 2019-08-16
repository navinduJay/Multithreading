import java.io.*;
// logger writes output to cron.log file
public class Logger
{
    private Object monitor = new Object();
    private String newMessage = null;
    private Thread thread = null;

    /**
     * Sets the next message to be logged. This method *may* have to wait for the previous message
     * to finish being logged, and hence it may throw InterruptedException (if the thread is
     * interrupted during the wait).
     */
    public void setMessage(String newMessage) throws InterruptedException
    {
        // We have to lock the monitor here, in line with the standard use of monitors for thread communication.
        synchronized(monitor)
        {
            if(this.newMessage != null)
            {
                // Wait for the logging thread to finish with the previous message and call
                // notify(). This also where InterruptedException originates from.

                // TODO: Add code to wait the current Job thread till the newMessage is consumed
                thread.currentThread().sleep(500);
            }

            this.newMessage = newMessage;

            // Notify the logging thread that the new message is now there to be logged.

            // TODO: Add code to notify the logger thread that there is a new message to be written to the log
            thread.currentThread().notify();
        }
    }

    /**
     * Starts up the logging thread. This opens 'cron.log', and waits for each new message to come
     * in (one by one), and writes it to the file.
     */
    public void start()
    {
        if(thread != null)
        {
            // Sanity check.
            throw new IllegalStateException("Logger already started");
        }

        // Define the task to be run in the logging thread.
        Runnable task = new Runnable()
        {
            @Override
            public void run()
            {
                /* First, we open the file.
                 *
                 * (This construct is a try-with-resources statement, which is an elegant way to
                 * handle opening and closing of resources like files. The 'writer' object will
                 * automatically have its close() method at the end, whether or not an exception
                 * occurs.)
                 */
                try(PrintWriter writer = new PrintWriter("cron.log"))
                {
                    // Second, we must lock the monitor, in line with standard use of monitors for thread communication.
                    synchronized(monitor)
                    {
                        // This is sort-of an infinite loop, but we expect InterruptedException to
                        // happen eventually, which will break out of it.
                        while(true)
                        {
                            if(newMessage == null)
                            {
                                // If there's no new message waiting for us, then wait until there
                                // is. This is effectively waiting for setMessage() to call
                                // notify().
                                //
                                // This is also the origin of an InterruptedException that will
                                // help the logger thread shut down at the appropriate time.
                                monitor.wait();
                            }

                            // Log message to file
                            writer.println(newMessage);

                            // Flush. (This prevents the file changes being cached in memory, and
                            // so allows you to see the changes to 'cron.log' in real time.)
                            writer.flush();

                            // TODO: Set the newMessage to null
                            newMessage = null;

                            // If there is another thread waiting to set the message, notify it.

                            // TODO: Notify a waiting job thread to set its message as a new message
                            thread.notify();
                        }
                    }
                }
                catch(IOException e)
                {
                    // Inevitably there could be IO errors writing to 'cron.log'. This is not the
                    // most sophisticated way of handling them, and in reality we would probably
                    // want to take steps to abort the program here (as there's no point
                    // continuing).
                    System.err.println("*** Cannot write to log file ***");
                }
                catch(InterruptedException e)
                {
                    // InterruptedException is our intended means of shutting down the logging
                    // thread normally, so we do in fact expect this to happen.
                    System.out.println("*** Cron ending ***");
                }

                thread = null;
            }
        };

        // Actually start the logging thread.
        thread = new Thread(task, "logger thread");
        thread.start();
    }

    /**
     * Shuts down the logging thread by interrupting it.
     */
    public void stop()
    {
        if(thread == null)
        {
            // Sanity check.
            throw new IllegalStateException("Logger already stopped");
        }

        // Generates an InterruptedException, not here, but rather inside the logging thread. This
        // will happen as soon as it calls wait(), or immediately if it's currently waiting.

        // TODO: Add code to interrupt the Job thread
        thread.interrupt();

    }
}
