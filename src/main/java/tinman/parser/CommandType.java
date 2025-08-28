package tinman.parser;

import tinman.exception.TinManException;
import tinman.task.Deadline;
import tinman.task.Event;
import tinman.task.Task;
import tinman.task.Todo;

public enum CommandType {
    TODO("todo", "T"),
    DEADLINE("deadline", "D"),
    EVENT("event", "E"),
    LIST("list", ""),
    MARK("mark", ""),
    UNMARK("unmark", ""),
    DELETE("delete", ""),
    BYE("bye", ""),
    UNKNOWN("", "");

    private final String keyword;
    private final String saveTypeCode;

    CommandType(String keyword, String saveTypeCode) {
        this.keyword = keyword;
        this.saveTypeCode = saveTypeCode;
    }

    public String getKeyword() {
        return keyword;
    }

    public static CommandType fromString(String command) {
        for (CommandType type : CommandType.values()) {
            if (type.keyword.equals(command.toLowerCase().trim())) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public static CommandType fromSaveTypeCode(String typeCode) {
        for (CommandType type : CommandType.values()) {
            if (type.saveTypeCode.equals(typeCode)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public Task createFromSaveFormat(String[] parts, boolean isDone) throws TinManException {
        switch (this) {
        case TODO:
            return Todo.fromSaveFormat(parts, isDone);
        case DEADLINE:
            return Deadline.fromSaveFormat(parts, isDone);
        case EVENT:
            return Event.fromSaveFormat(parts, isDone);
        default:
            throw new TinManException("Unknown task type in data file: " + this.saveTypeCode);
        }
    }
}
