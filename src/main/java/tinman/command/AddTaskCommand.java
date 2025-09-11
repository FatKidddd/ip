package tinman.command;

import tinman.exception.TinManException;
import tinman.parser.Parser;
import tinman.task.Task;
import tinman.task.TaskList;

/**
 * Command to add a new task (todo, deadline, or event) to the task list.
 */
public class AddTaskCommand implements Command {
    @Override
    public String execute(TaskList tasks, String input) throws TinManException {
        Task task = Parser.parseTask(input);
        tasks.addTask(task);
        int taskCount = tasks.getTaskCount();
        return "Got it. I've added this task:\n  " + task
                + "\nNow you have " + formatTaskCountMessage(taskCount) + " in the list.";
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
