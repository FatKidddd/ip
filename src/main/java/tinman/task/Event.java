package tinman.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import tinman.exception.TinManException;
import tinman.util.DateParser;

public class Event extends Task {
    private String from;
    private String to;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;

    public Event(String description, String from, String to) {
        super(description);
        try {
            Object parsedFrom = DateParser.parseFlexible(from);
            if (parsedFrom instanceof LocalDateTime) {
                this.fromDateTime = (LocalDateTime) parsedFrom;
                this.fromDate = null;
            } else if (parsedFrom instanceof LocalDate) {
                this.fromDate = (LocalDate) parsedFrom;
                this.fromDateTime = null;
            }
            this.from = null;
        } catch (TinManException e) {
            this.from = from;
            this.fromDate = null;
            this.fromDateTime = null;
        }
        
        try {
            Object parsedTo = DateParser.parseFlexible(to);
            if (parsedTo instanceof LocalDateTime) {
                this.toDateTime = (LocalDateTime) parsedTo;
                this.toDate = null;
            } else if (parsedTo instanceof LocalDate) {
                this.toDate = (LocalDate) parsedTo;
                this.toDateTime = null;
            }
            this.to = null;
        } catch (TinManException e) {
            this.to = to;
            this.toDate = null;
            this.toDateTime = null;
        }
    }

    @Override
    public String toString() {
        String fromDisplay;
        if (fromDateTime != null) {
            fromDisplay = DateParser.formatDateTime(fromDateTime);
        } else if (fromDate != null) {
            fromDisplay = DateParser.formatDate(fromDate);
        } else {
            fromDisplay = from;
        }
        
        String toDisplay;
        if (toDateTime != null) {
            toDisplay = DateParser.formatDateTime(toDateTime);
        } else if (toDate != null) {
            toDisplay = DateParser.formatDate(toDate);
        } else {
            toDisplay = to;
        }
        
        return "[E]" + super.toString() + " (from: " + fromDisplay + " to: " + toDisplay + ")";
    }

    @Override
    public String getTaskType() {
        return "E";
    }

    @Override
    public String toSaveFormat() {
        String status = isDone ? "1" : "0";
        
        String fromToSave;
        if (fromDateTime != null) {
            fromToSave = DateParser.dateTimeToSaveFormat(fromDateTime);
        } else if (fromDate != null) {
            fromToSave = DateParser.dateToSaveFormat(fromDate);
        } else {
            fromToSave = from;
        }
        
        String toToSave;
        if (toDateTime != null) {
            toToSave = DateParser.dateTimeToSaveFormat(toDateTime);
        } else if (toDate != null) {
            toToSave = DateParser.dateToSaveFormat(toDate);
        } else {
            toToSave = to;
        }
        
        return getTaskType() + " | " + status + " | " + getDescription() + " | " + fromToSave + " | " + toToSave;
    }

    public static Event fromSaveFormat(String[] parts, boolean isDone) throws TinManException {
        if (parts.length < 5) {
            throw new TinManException("Invalid event format in data file");
        }
        
        // The Event constructor will handle parsing the date/time strings automatically
        Event task = new Event(parts[2], parts[3], parts[4]);
        
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
