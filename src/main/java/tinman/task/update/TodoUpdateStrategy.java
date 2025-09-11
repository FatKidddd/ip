package tinman.task.update;

import tinman.exception.TinManException;
import tinman.task.Task;

/**
 * Update strategy for Todo tasks.
 * Supports updating description only.
 */
public class TodoUpdateStrategy implements UpdateStrategy {
    @Override
    public boolean canHandle(String parameters) {
        return parameters.startsWith("/desc ");
    }

    @Override
    public void update(Task task, String parameters) throws TinManException {
        // We know it starts with "/desc " because canHandle() already validated it
        String newDescription = parameters.substring(6).trim();
        if (newDescription.isEmpty()) {
            throw new TinManException("Description cannot be empty.");
        }
        task.updateDescription(newDescription);
    }

    @Override
    public String getAvailableOptions() {
        return "/desc <new description>";
    }
}