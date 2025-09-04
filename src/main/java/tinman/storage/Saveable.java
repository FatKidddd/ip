package tinman.storage;

import tinman.exception.TinManException;
import tinman.parser.CommandType;
import tinman.task.Task;

/**
 * Interface for objects that can be saved to and loaded from a file format.
 */
public interface Saveable {
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
        if (parts.length < 3) {
            throw new TinManException("Invalid task format in data file");
        }

        String typeCode = parts[0];
        boolean isDone = "1".equals(parts[1]);

        CommandType commandType = CommandType.parseSaveTypeCode(typeCode);
        if (commandType == CommandType.UNKNOWN) {
            throw new TinManException("Unknown task type in data file: " + typeCode);
        }

        return commandType.createFromSaveFormat(parts, isDone);
    }
}

