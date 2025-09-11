package tinman.command;

import tinman.exception.TinManException;
import tinman.parser.Parser;
import tinman.task.Task;
import tinman.task.TaskList;

/**
 * Command to mark a task as not done.
 */
public class UnmarkCommand implements Command {
    @Override
    public String execute(TaskList tasks, String input) throws TinManException {
        int taskIndex = Parser.parseTaskNumber(input);
        Task task = tasks.getTask(taskIndex);
        task.markAsNotDone();
        return "OK, I've marked this task as not done yet:\n  " + task;
    }
}