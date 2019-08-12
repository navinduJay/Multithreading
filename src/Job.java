import java.io.*;

/**
 * Represents a job (command) to be run, and performs the execution. (You need to fill in the 
 * details!)
 */
public class Job implements Runnable
{
    private String command;
    private int delay;
    private Logger logger;

    public Job(String command, int delay, Logger logger) {
        this.command = command;
        this.delay = delay;
        this.logger = logger;
    }

    @Override
    public void run() {

        // Assume 'command' is a string containing the command the
        // execute. Then we initially run it as follows:
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Arrange to capture the command's output, line by line.
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(line != null)
        {
            output.append(line);
            output.append('\n');
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // We've now reached the end of the command's output, which
        // generally means the command has finished.
        System.out.println(command + ": " + output.toString());
    }
        // ...
}
