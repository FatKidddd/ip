package tinman.command;

import tinman.exception.TinManException;
import tinman.parser.Parser;
import tinman.task.Deadline;
import tinman.task.Event;
import tinman.task.Task;
import tinman.task.TaskList;

/**
 * Command to update an existing task.
 * Uses a parameter-first approach to determine what to update.
 */
public class UpdateCommand implements Command {

    @Override
    public String execute(TaskList tasks, String input) throws TinManException {
        try {
            String[] updateParts = Parser.parseUpdateCommand(input);
            int taskIndex = Integer.parseInt(updateParts[0]) - 1;
            String parameters = updateParts[1];

            Task task = tasks.getTask(taskIndex);

            // Parameter-first approach - check what user wants to update
            if (hasParameter(parameters, "/desc ")) {
                updateDescription(task, parameters);
            } else if (hasParameter(parameters, "/by ")) {
                updateDeadline(task, parameters);
            } else if (hasParameter(parameters, "/from ") && hasParameter(parameters, "/to ")) {
                updateEventTimes(task, parameters);
            } else {
                throw new TinManException("Invalid update parameters. Available formats: "
                        + getAvailableOptionsForTask(task));
            }

            return "Got it! I've updated this task:\n  " + task;
        } catch (NumberFormatException e) {
            throw new TinManException("Invalid task number format.");
        }
    }

    /**
     * Updates the description of any task type.
     *
     * @param task The task to update.
     * @param parameters The parameters containing the new description.
     * @throws TinManException If the description is empty.
     */
    private void updateDescription(Task task, String parameters) throws TinManException {
        String[] parts = Parser.extractParts(parameters, "/desc ", "update", "/desc <description>");
        String newDescription = parts[1].trim();
        if (newDescription.isEmpty()) {
            throw new TinManException("Description cannot be empty.");
        }
        task.updateDescription(newDescription);
    }

    /**
     * Updates the deadline of a Deadline task.
     *
     * @param task The task to update.
     * @param parameters The parameters containing the new deadline.
     * @throws TinManException If the task is not a Deadline or the deadline is empty.
     */
    private void updateDeadline(Task task, String parameters) throws TinManException {
        if (!(task instanceof Deadline)) {
            throw new TinManException("Cannot set deadline on " + getTaskTypeName(task)
                    + " task. Available formats: " + getAvailableOptionsForTask(task));
        }

        String[] parts = Parser.extractParts(parameters, "/by ", "update", "/by <date>");
        String newDeadline = parts[1].trim();
        if (newDeadline.isEmpty()) {
            throw new TinManException("Deadline cannot be empty.");
        }

        Deadline deadline = (Deadline) task;
        deadline.updateDeadline(newDeadline);
    }

    /**
     * Updates the times of an Event task.
     *
     * @param task The task to update.
     * @param parameters The parameters containing the new times.
     * @throws TinManException If the task is not an Event or the parameters are invalid.
     */
    private void updateEventTimes(Task task, String parameters) throws TinManException {
        if (!(task instanceof Event)) {
            throw new TinManException("Cannot set event times on " + getTaskTypeName(task)
                    + " task. Available formats: " + getAvailableOptionsForTask(task));
        }

        Event event = (Event) task;

        if (parameters.startsWith("/from ")) {
            // Only updating times: "/from time /to time"
            updateTimesOnly(event, parameters);
        } else {
            // Description + times: "description /from time /to time"
            updateDescriptionAndTimes(event, parameters);
        }
    }

    /**
     * Updates only the event times.
     */
    private void updateTimesOnly(Event event, String parameters) throws TinManException {
        String[] fromParts = Parser.extractParts(parameters, "/from ", "event", "/from <start> /to <end>");
        String[] toParts = Parser.extractParts(fromParts[1], " /to ", "event", "/from <start> /to <end>");

        String newFrom = toParts[0].trim();
        String newTo = toParts[1].trim();

        if (newFrom.isEmpty() || newTo.isEmpty()) {
            throw new TinManException("Event times cannot be empty.");
        }

        event.updateFrom(newFrom);
        event.updateTo(newTo);
    }

    /**
     * Updates both description and event times.
     */
    private void updateDescriptionAndTimes(Event event, String parameters) throws TinManException {
        String[] fromParts = Parser.extractParts(
                parameters, " /from ", "event", "<description> /from <start> /to <end>");
        String description = fromParts[0].trim();
        String[] toParts = Parser.extractParts(fromParts[1], " /to ", "event", "/from <start> /to <end>");

        String newFrom = toParts[0].trim();
        String newTo = toParts[1].trim();

        if (description.isEmpty() || newFrom.isEmpty() || newTo.isEmpty()) {
            throw new TinManException("Description and event times cannot be empty.");
        }

        event.updateDescription(description);
        event.updateFrom(newFrom);
        event.updateTo(newTo);
    }

    /**
     * Checks if parameters contain a specific parameter prefix.
     */
    private boolean hasParameter(String parameters, String prefix) {
        return parameters.indexOf(prefix) != -1;
    }

    /**
     * Gets the available update options for a specific task type.
     */
    private String getAvailableOptionsForTask(Task task) {
        if (task instanceof Event) {
            return "/desc <new description>, /from <start> /to <end>, "
                    + "or <description> /from <start> /to <end>";
        } else if (task instanceof Deadline) {
            return "/desc <new description> or /by <new date>";
        } else {
            return "/desc <new description>";
        }
    }

    /**
     * Gets a user-friendly name for the task type.
     */
    private String getTaskTypeName(Task task) {
        if (task instanceof Event) {
            return "event";
        } else if (task instanceof Deadline) {
            return "deadline";
        } else {
            return "todo";
        }
    }
}
