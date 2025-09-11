package tinman.command;

import tinman.exception.TinManException;
import tinman.parser.Parser;
import tinman.task.Task;
import tinman.task.TaskList;

/**
 * Command to mark a task as done.
 */
public class MarkCommand implements Command {
    @Override
    public String execute(TaskList tasks, String input) throws TinManException {
        int taskIndex = Parser.parseTaskNumber(input);
        Task task = tasks.getTask(taskIndex);
        task.markAsDone();
        return "Nice! I've marked this task as done:\n  " + task;
    }
}
