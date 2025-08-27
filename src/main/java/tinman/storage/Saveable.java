package tinman.storage;

import tinman.task.Task;
import tinman.parser.CommandType;
import tinman.exception.TinManException;

public interface Saveable {
    String toSaveFormat();
    
    static Task fromSaveFormat(String line) throws TinManException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new TinManException("Invalid task format in data file");
        }
        
        String typeCode = parts[0];
        boolean isDone = "1".equals(parts[1]);
        
        CommandType commandType = CommandType.fromSaveTypeCode(typeCode);
        if (commandType == CommandType.UNKNOWN) {
            throw new TinManException("Unknown task type in data file: " + typeCode);
        }
        
        return commandType.createFromSaveFormat(parts, isDone);
    }
}