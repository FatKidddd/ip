public class Deadline extends Task {
    public String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    @Override
    public String getTaskType() {
        return "D";
    }

    @Override
    public String toSaveFormat() {
        String status = isDone ? "1" : "0";
        return getTaskType() + " | " + status + " | " + getDescription() + " | " + by;
    }

    public static Deadline fromSaveFormat(String[] parts, boolean isDone) throws TinManException {
        if (parts.length < 4) {
            throw new TinManException("Invalid deadline format in data file");
        }
        Deadline task = new Deadline(parts[2], parts[3]);
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
