package tinman.task;

import tinman.exception.TinManException;

/**
 * Represents a simple todo task without any date/time constraints.
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String getTaskType() {
        return "T";
    }

    /**
     * Creates a Todo from save format data.
     *
     * @param parts Array of strings containing todo data.
     * @param isDone Whether the todo is completed.
     * @return Todo object created from save format.
     * @throws TinManException If the save format is invalid.
     */
    public static Todo fromSaveFormat(String[] parts, boolean isDone) throws TinManException {
        if (parts.length < 3) {
            throw new TinManException("Invalid todo format in data file");
        }
        Todo task = new Todo(parts[2]);
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
