package tinman.command;

import tinman.exception.TinManException;
import tinman.parser.Parser;
import tinman.task.Task;
import tinman.task.TaskList;

/**
 * Command to delete a task from the task list.
 */
public class DeleteCommand implements Command {
    @Override
    public String execute(TaskList tasks, String input) throws TinManException {
        int taskIndex = Parser.parseTaskNumber(input);
        Task deletedTask = tasks.deleteTask(taskIndex);
        int remainingCount = tasks.getTaskCount();
        return "Noted. I've removed this task:\n  " + deletedTask
                + "\nNow you have " + formatTaskCountMessage(remainingCount) + " in the list.";
    }

    /**
     * Formats the task count message with proper singular/plural form.
     *
     * @param count The number of tasks.
     * @return Formatted task count message.
     */
    private String formatTaskCountMessage(int count) {
        String taskWord = (count == 1) ? "task" : "tasks";
        return count + " " + taskWord;
    }
}
