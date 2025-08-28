package tinman.task;

import tinman.exception.TinManException;
import tinman.storage.Saveable;
import tinman.util.DateParser;

public class Task implements Saveable {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
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
