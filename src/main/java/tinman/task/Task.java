package tinman.task;

import tinman.exception.TinManException;
import tinman.storage.Saveable;

/**
 * Represents a task with a description and completion status.
 * This is the base class for all types of tasks.
 */
public class Task implements Saveable {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task with the given description.
     * The task is initially marked as not done.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon representing the completion status.
     *
     * @return "X" if task is done, " " if not done.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsDone() {
        return isDone;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    @Override
    public String toSaveFormat() {
        String status = isDone ? "1" : "0";
        return getTaskType() + " | " + status + " | " + description;
    }

    public String getTaskType() {
        return "T";
    }

    /**
     * Creates a Task from save format data.
     *
     * @param parts Array of strings containing task data.
     * @param isDone Whether the task is completed.
     * @return Task object created from save format.
     * @throws TinManException If the save format is invalid.
     */
    public static Task fromSaveFormat(String[] parts, boolean isDone) throws TinManException {
        if (parts.length < 3) {
            throw new TinManException("Invalid task format in data file");
        }
        Task task = new Todo(parts[2]);
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
