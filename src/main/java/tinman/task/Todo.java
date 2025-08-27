package tinman.task;

import tinman.exception.TinManException;

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
