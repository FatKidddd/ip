package tinman.task.update;

import tinman.exception.TinManException;
import tinman.task.Task;

/**
 * Strategy interface for updating different types of tasks.
 * Each task type (Todo, Deadline, Event) has its own update strategy.
 */
public interface UpdateStrategy {
    /**
     * Checks if this strategy can handle the given parameters.
     *
     * @param parameters The update parameters string.
     * @return True if this strategy can handle the parameters.
     */
    boolean canHandle(String parameters);

    /**
     * Updates a task with the given parameters.
     *
     * @param task The task to update.
     * @param parameters The update parameters string.
     * @throws TinManException If the update parameters are invalid or update fails.
     */
    void update(Task task, String parameters) throws TinManException;

    /**
     * Returns the available update options for this task type.
     *
     * @return A string describing the available update formats.
     */
    String getAvailableOptions();
}