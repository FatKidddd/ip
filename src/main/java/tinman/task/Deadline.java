package tinman.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import tinman.exception.TinManException;
import tinman.util.DateParser;

/**
 * Represents a deadline task with a specific due date/time.
 */
public class Deadline extends Task {
    private LocalDate by;
    private LocalDateTime byDateTime;
    private String byString;

    /**
     * Constructs a Deadline with the given description and due date string.
     *
     * @param description Description of the deadline task.
     * @param by Due date/time as a string.
     * @throws TinManException If date parsing fails.
     */
    public Deadline(String description, String by) throws TinManException {
        super(description);
        try {
            Object parsed = DateParser.parseFlexible(by);
            if (parsed instanceof LocalDateTime) {
                this.byDateTime = (LocalDateTime) parsed;
                this.by = null;
            } else if (parsed instanceof LocalDate) {
                this.by = (LocalDate) parsed;
                this.byDateTime = null;
            }
            this.byString = null;
        } catch (TinManException e) {
            this.by = null;
            this.byDateTime = null;
            this.byString = by;
        }
    }

    /**
     * Constructs a Deadline with the given description and due date.
     *
     * @param description Description of the deadline task.
     * @param by Due date.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
        this.byDateTime = null;
        this.byString = null;
    }

    /**
     * Constructs a Deadline with the given description and due date/time.
     *
     * @param description Description of the deadline task.
     * @param byDateTime Due date and time.
     */
    public Deadline(String description, LocalDateTime byDateTime) {
        super(description);
        this.by = null;
        this.byDateTime = byDateTime;
        this.byString = null;
    }

    @Override
    public String toString() {
        String dateDisplay;
        if (byDateTime != null) {
            dateDisplay = DateParser.formatDateTime(byDateTime);
        } else if (by != null) {
            dateDisplay = DateParser.formatDate(by);
        } else {
            dateDisplay = byString;
        }
        return "[D]" + super.toString() + " (by: " + dateDisplay + ")";
    }

    @Override
    public String getTaskType() {
        return "D";
    }

    @Override
    public String toSaveFormat() {
        String status = isDone ? "1" : "0";
        String dateToSave;
        if (byDateTime != null) {
            dateToSave = DateParser.dateTimeToSaveFormat(byDateTime);
        } else if (by != null) {
            dateToSave = DateParser.dateToSaveFormat(by);
        } else {
            dateToSave = byString;
        }
        return getTaskType() + " | " + status + " | " + getDescription() + " | " + dateToSave;
    }

    /**
     * Creates a Deadline from save format data.
     *
     * @param parts Array of strings containing deadline data.
     * @param isDone Whether the deadline is completed.
     * @return Deadline object created from save format.
     * @throws TinManException If the save format is invalid.
     */
    public static Deadline fromSaveFormat(String[] parts, boolean isDone) throws TinManException {
        if (parts.length < 4) {
            throw new TinManException("Invalid deadline format in data file");
        }
        
        Deadline task;
        try {
            // Try datetime first, then date
            if (DateParser.isValidDateTimeFormat(parts[3])) {
                LocalDateTime dateTime = DateParser.dateTimeFromSaveFormat(parts[3]);
                task = new Deadline(parts[2], dateTime);
            } else if (DateParser.isValidDateFormat(parts[3])) {
                LocalDate date = DateParser.dateFromSaveFormat(parts[3]);
                task = new Deadline(parts[2], date);
            } else {
                // Fall back to string
                task = new Deadline(parts[2], parts[3]);
            }
        } catch (TinManException e) {
            task = new Deadline(parts[2], parts[3]);
        }
        
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
