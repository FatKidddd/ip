package tinman.task.update;

import tinman.exception.TinManException;
import tinman.task.Deadline;
import tinman.task.Task;

/**
 * Update strategy for Deadline tasks.
 * Supports updating description or deadline date.
 */
public class DeadlineUpdateStrategy implements UpdateStrategy {
    @Override
    public boolean canHandle(String parameters) {
        return parameters.startsWith("/desc ") || parameters.startsWith("/by ");
    }

    @Override
    public void update(Task task, String parameters) throws TinManException {
        Deadline deadline = (Deadline) task;

        if (parameters.startsWith("/desc ")) {
            String newDescription = parameters.substring(6).trim();
            if (newDescription.isEmpty()) {
                throw new TinManException("Description cannot be empty.");
            }
            deadline.updateDescription(newDescription);
        } else { // Must be "/by " since canHandle() validated it
            String newDeadline = parameters.substring(4).trim();
            if (newDeadline.isEmpty()) {
                throw new TinManException("Deadline cannot be empty.");
            }
            deadline.updateDeadline(newDeadline);
        }
    }

    @Override
    public String getAvailableOptions() {
        return "/desc <new description> or /by <new date>";
    }
}