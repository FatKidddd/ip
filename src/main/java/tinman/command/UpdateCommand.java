package tinman.command;

import java.util.HashMap;
import java.util.Map;

import tinman.exception.TinManException;
import tinman.parser.Parser;
import tinman.task.Deadline;
import tinman.task.Event;
import tinman.task.Task;
import tinman.task.TaskList;
import tinman.task.Todo;
import tinman.task.update.DeadlineUpdateStrategy;
import tinman.task.update.EventUpdateStrategy;
import tinman.task.update.TodoUpdateStrategy;
import tinman.task.update.UpdateStrategy;

/**
 * Command to update an existing task.
 * Uses the Strategy pattern to handle different task types.
 */
public class UpdateCommand implements Command {
    private final Map<Class<? extends Task>, UpdateStrategy> strategies;

    /**
     * Creates an UpdateCommand and initializes update strategies.
     */
    public UpdateCommand() {
        strategies = new HashMap<>();
        strategies.put(Todo.class, new TodoUpdateStrategy());
        strategies.put(Deadline.class, new DeadlineUpdateStrategy());
        strategies.put(Event.class, new EventUpdateStrategy());
    }

    @Override
    public String execute(TaskList tasks, String input) throws TinManException {
        try {
            String[] updateParts = Parser.parseUpdateCommand(input);
            int taskIndex = Integer.parseInt(updateParts[0]) - 1;
            String parameters = updateParts[1];

            // First get the task to determine its type
            Task task = tasks.getTask(taskIndex);
            
            // Then get the appropriate strategy based on the actual task type
            UpdateStrategy strategy = getStrategyForTask(task);
            
            // Finally, let the strategy handle parsing and updating
            strategy.update(task, parameters);

            return "Got it! I've updated this task:\n  " + task;
        } catch (NumberFormatException e) {
            throw new TinManException("Invalid task number format.");
        }
    }

    /**
     * Gets the appropriate update strategy for the given task.
     *
     * @param task The task to get a strategy for.
     * @return The update strategy for the task type.
     * @throws TinManException If no strategy is found for the task type.
     */
    private UpdateStrategy getStrategyForTask(Task task) throws TinManException {
        UpdateStrategy strategy = strategies.get(task.getClass());
        if (strategy == null) {
            throw new TinManException("No update strategy found for task type: " + task.getClass().getSimpleName());
        }
        return strategy;
    }
}