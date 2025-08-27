public class Event extends Task {
    public String from;
    public String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    @Override
    public String getTaskType() {
        return "E";
    }

    @Override
    public String toSaveFormat() {
        String status = isDone ? "1" : "0";
        return getTaskType() + " | " + status + " | " + getDescription() + " | " + from + " | " + to;
    }

    public static Event fromSaveFormat(String[] parts, boolean isDone) throws TinManException {
        if (parts.length < 5) {
            throw new TinManException("Invalid event format in data file");
        }
        Event task = new Event(parts[2], parts[3], parts[4]);
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
