package mp.app.tasks;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import io.dropwizard.servlets.tasks.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;

public class MyTask extends Task {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Create a new task with the given name.
     *
     * @param name the task's name
     */
    public MyTask() {
        super("do-stuff");
    }

    @Override
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        ImmutableCollection<String> args = parameters.get("args").asList();
        if (args.isEmpty()) {
            output.println("you have to pass some \"args\" (.../do-stuff?args=bla,blub)");
        }
        int i=1;
        for (String arg : args) {
            logger.info("Loop {} with arg: {}",i,arg);
            output.println("Loop "+i+" with arg: "+arg);
            output.flush();
            i++;
        }
        output.flush();
    }
}
