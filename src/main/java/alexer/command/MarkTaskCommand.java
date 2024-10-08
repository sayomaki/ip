package alexer.command;

import alexer.Alexer;
import alexer.task.Task;
import alexer.task.TaskManager;
import alexer.ui.Response;

import static alexer.Prompter.MESSAGE_MARK_TASK;

/**
 * Command to mark tasks as done. Does nothing if
 * task is already marked as done.
 *
 * <p>
 * Usage: mark
 *      where n is the index of the task
 * Example: mark 3
 * </p>
 *
 * @author sayomaki
 */
public class MarkTaskCommand extends Command {
    public MarkTaskCommand() {
        super("mark");
    }

    @Override
    public Response run(String... arguments) {
        try {
            int index = Integer.parseInt(arguments[0]);
            TaskManager taskManager = Alexer.getInstance().getTaskManager();

            Task task = taskManager.getTask(index - 1);
            if (task == null) {
                return new Response("Error: I cannot mark a task that is non-existent.",
                        Response.ResponseType.ERROR);
            }

            task.markAsDone();
            taskManager.saveTasks();

            return new Response(String.format("%s\n\n\t%s",
                    MESSAGE_MARK_TASK, taskManager.getTask(index - 1)));
        } catch (NumberFormatException e) {
            return new Response("Error: That does not seem to be a valid task index, please try again!",
                    Response.ResponseType.ERROR);
        }
    }
}
