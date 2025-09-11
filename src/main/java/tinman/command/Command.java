package tinman.command;

import tinman.exception.TinManException;
import tinman.task.TaskList;

/**
 * Represents a command that can be executed in the TinMan application.
 * Each command encapsulates the logic for a specific user action.
 */
public interface Command {
    /**
     * Executes the command with the given parameters.
     *
     * @param tasks The task list to operate on.
     * @param input The full user input string.
     * @return The result message to display to the user.
     * @throws TinManException If the command execution fails.
     */
    String execute(TaskList tasks, String input) throws TinManException;
}
