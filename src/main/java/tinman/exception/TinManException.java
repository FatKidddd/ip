package tinman.exception;

/**
 * Custom exception class for TinMan application errors.
 */
public class TinManException extends Exception {
    public TinManException(String message) {
        super(message);
    }

    /**
     * Represents an exception thrown when a task description is empty.
     */
    public static class EmptyDescriptionException extends TinManException {
        public EmptyDescriptionException(String taskType) {
            super("OOPS!!! The description of a " + taskType + " cannot be empty.");
        }
    }

    /**
     * Represents an exception thrown when an invalid task number is provided.
     */
    public static class InvalidTaskNumberException extends TinManException {
        public InvalidTaskNumberException() {
            super("OOPS!!! Please provide a valid task number.");
        }
    }

    /**
     * Represents an exception thrown when a requested task cannot be found.
     */
    public static class TaskNotFoundException extends TinManException {
        public TaskNotFoundException() {
            super("OOPS!!! Task not found. Please check the task number.");
        }
    }

    /**
     * Represents an exception thrown when command format is invalid.
     */
    public static class InvalidFormatException extends TinManException {
        public InvalidFormatException(String correctFormat) {
            super("OOPS!!! Invalid format. Please use: " + correctFormat);
        }
    }

    /**
     * Represents an exception thrown when task list capacity is exceeded.
     */
    public static class TaskListFullException extends TinManException {
        public TaskListFullException() {
            super("OOPS!!! Task list is full. Cannot add more tasks.");
        }
    }

    /**
     * Represents an exception thrown when an unknown command is encountered.
     */
    public static class UnknownCommandException extends TinManException {
        public UnknownCommandException() {
            super("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Represents an exception thrown when date format is invalid.
     */
    public static class InvalidDateFormatException extends TinManException {
        public InvalidDateFormatException() {
            super("OOPS!!! Invalid date format. Please use yyyy-mm-dd format (e.g., 2019-12-02).");
        }
    }
}
