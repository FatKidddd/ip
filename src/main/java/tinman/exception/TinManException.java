package tinman.exception;

public class TinManException extends Exception {
    public TinManException(String message) {
        super(message);
    }

    public static class EmptyDescriptionException extends TinManException {
        public EmptyDescriptionException(String taskType) {
            super("OOPS!!! The description of a " + taskType + " cannot be empty.");
        }
    }

    public static class InvalidTaskNumberException extends TinManException {
        public InvalidTaskNumberException() {
            super("OOPS!!! Please provide a valid task number.");
        }
    }

    public static class TaskNotFoundException extends TinManException {
        public TaskNotFoundException() {
            super("OOPS!!! Task not found. Please check the task number.");
        }
    }

    public static class InvalidFormatException extends TinManException {
        public InvalidFormatException(String correctFormat) {
            super("OOPS!!! Invalid format. Please use: " + correctFormat);
        }
    }

    public static class TaskListFullException extends TinManException {
        public TaskListFullException() {
            super("OOPS!!! Task list is full. Cannot add more tasks.");
        }
    }

    public static class UnknownCommandException extends TinManException {
        public UnknownCommandException() {
            super("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    public static class InvalidDateFormatException extends TinManException {
        public InvalidDateFormatException() {
            super("OOPS!!! Invalid date format. Please use yyyy-mm-dd format (e.g., 2019-12-02).");
        }
    }
}
