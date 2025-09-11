package tinman.command;

import tinman.task.TaskList;

/**
 * Command to handle the bye/exit command.
 */
public class ByeCommand implements Command {
    @Override
    public String execute(TaskList tasks, String input) {
        return "Bye. Hope to see you again soon!";
    }
}
