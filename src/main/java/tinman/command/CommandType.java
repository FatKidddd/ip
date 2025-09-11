package tinman.command;

import tinman.exception.TinManException;
import tinman.task.Deadline;
import tinman.task.Event;
import tinman.task.Task;
import tinman.task.Todo;

/**
 * Represents the different types of commands that can be executed.
 */
public enum CommandType {
    TODO("todo", "T"),
    DEADLINE("deadline", "D"),
    EVENT("event", "E"),
    LIST("list", ""),
    MARK("mark", ""),
    UNMARK("unmark", ""),
    DELETE("delete", ""),
    FIND("find", ""),
    UPDATE("update", ""),
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

    /**
     * Parses a command string to determine the CommandType.
     *
     * @param command The command string to parse.
     * @return The corresponding CommandType, or UNKNOWN if not recognized.
     */
    public static CommandType parseString(String command) {
        for (CommandType type : CommandType.values()) {
            if (type.keyword.equals(command.toLowerCase().trim())) {
                return type;
            }
        }
        return UNKNOWN;
    }

    /**
     * Parses a save type code to determine the CommandType.
     *
     * @param typeCode The type code from saved data.
     * @return The corresponding CommandType, or UNKNOWN if not recognized.
     */
    public static CommandType parseSaveTypeCode(String typeCode) {
        for (CommandType type : CommandType.values()) {
            if (type.saveTypeCode.equals(typeCode)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    /**
     * Creates a Task from save format data based on this command type.
     *
     * @param parts Array of strings containing task data.
     * @param isDone Whether the task is completed.
     * @return Task object created from save format.
     * @throws TinManException If the task type is unknown or data is invalid.
     */
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
