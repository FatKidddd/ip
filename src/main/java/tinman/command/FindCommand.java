package tinman.command;

import java.util.ArrayList;

import tinman.exception.TinManException;
import tinman.task.Task;
import tinman.task.TaskList;

/**
 * Command to find tasks containing a specific keyword.
 */
public class FindCommand implements Command {
    private static final int FIND_COMMAND_LENGTH = 4;

    @Override
    public String execute(TaskList tasks, String input) throws TinManException {
        String keyword = extractSearchKeyword(input);
        if (keyword.isEmpty()) {
            throw new TinManException("Please provide a keyword to search for.");
        }

        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        return formatFindResults(matchingTasks);
    }

    /**
     * Extracts the search keyword from the find command input.
     * Removes the "find" prefix and trims whitespace.
     *
     * @param input The full find command input.
     * @return The search keyword.
     */
    private String extractSearchKeyword(String input) {
        return input.substring(FIND_COMMAND_LENGTH).trim();
    }

    /**
     * Formats the find results for display.
     *
     * @param matchingTasks List of tasks that match the search criteria.
     * @return Formatted find results string.
     */
    private String formatFindResults(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            return "Here are the matching tasks in your list:\n (no matching tasks found)";
        }

        StringBuilder result = new StringBuilder("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            result.append("\n ").append(i + 1).append(".").append(matchingTasks.get(i));
        }
        return result.toString();
    }
}