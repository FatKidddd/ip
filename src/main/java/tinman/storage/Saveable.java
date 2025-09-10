package tinman.storage;

import tinman.exception.TinManException;
import tinman.parser.CommandType;
import tinman.task.Task;

/**
 * Interface for objects that can be saved to and loaded from a file format.
 */
public interface Saveable {
    String SAVE_FORMAT_SEPARATOR = " | ";
    String DONE_INDICATOR = "1";
    int MINIMUM_SAVE_FORMAT_PARTS = 3;
    int TYPE_CODE_INDEX = 0;
    int STATUS_INDEX = 1;
    String toSaveFormat();

    /**
     * Creates a Task from a save format line.
     *
     * @param line The line containing task data in save format.
     * @return Task object created from the save format.
     * @throws TinManException If the save format is invalid.
     */
    static Task fromSaveFormat(String line) throws TinManException {
        String[] parts = line.split(" \\| ");
        if (parts.length < MINIMUM_SAVE_FORMAT_PARTS) {
            throw new TinManException("Invalid task format in data file");
        }

        String typeCode = parts[TYPE_CODE_INDEX];
        boolean isDone = DONE_INDICATOR.equals(parts[STATUS_INDEX]);

        CommandType commandType = CommandType.parseSaveTypeCode(typeCode);
        if (commandType == CommandType.UNKNOWN) {
            throw new TinManException("Unknown task type in data file: " + typeCode);
        }

        return commandType.createFromSaveFormat(parts, isDone);
    }
}

