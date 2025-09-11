package tinman.command;

import tinman.task.TaskList;

/**
 * Command to list all tasks in the task list.
 */
public class ListCommand implements Command {
    @Override
    public String execute(TaskList tasks, String input) {
        return tasks.listTasks();
    }
}