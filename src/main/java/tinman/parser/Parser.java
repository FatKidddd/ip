package tinman.parser;

import tinman.exception.TinManException;
import tinman.task.Deadline;
import tinman.task.Event;
import tinman.task.Task;
import tinman.task.Todo;

/**
 * Handles parsing of user input into commands and tasks.
 * Provides methods to parse various task types and command inputs.
 */
public class Parser {
    private static void validateDescription(String description, String taskType) throws TinManException {
        if (description.isEmpty()) {
            throw new TinManException.EmptyDescriptionException(taskType);
        }
    }

    private static String[] extractParts(String input, String delimiter, String taskType, String format)
            throws TinManException {
        int delimiterIndex = input.indexOf(delimiter);
        if (delimiterIndex == -1) {
            throw new TinManException.InvalidFormatException(format);
        }
        return new String[] {
                input.substring(0, delimiterIndex).trim(),
                input.substring(delimiterIndex + delimiter.length()).trim()
        };
    }

    private static Task parseTodo(String input) throws TinManException {
        String description = input.substring(5).trim();
        validateDescription(description, "todo");
        return new Todo(description);
    }

    private static Task parseDeadline(String input) throws TinManException {
        String fullCommand = input.substring(9).trim();
        validateDescription(fullCommand, "deadline");

        String[] parts = extractParts(fullCommand, " /by ", "deadline", "deadline <description> /by <time>");
        validateDescription(parts[0], "deadline");
        validateDescription(parts[1], "deadline");

        return new Deadline(parts[0], parts[1]);
    }

    private static Task parseEvent(String input) throws TinManException {
        String fullCommand = input.substring(6).trim();
        validateDescription(fullCommand, "event");

        String[] fromParts = extractParts(fullCommand, " /from ", "event",
                "event <description> /from <start> /to <end>");
        String[] toParts = extractParts(fromParts[1], " /to ", "event", "event <description> /from <start> /to <end>");

        String description = fromParts[0];
        String from = toParts[0];
        String to = toParts[1];

        validateDescription(description, "event");
        if (from.isEmpty() || to.isEmpty()) {
            throw new TinManException.InvalidFormatException("event <description> /from <start> /to <end>");
        }

        return new Event(description, from, to);
    }

    /**
     * Parses user input into a Task object.
     *
     * @param input The user input string containing task information.
     * @return A Task object representing the parsed task.
     * @throws TinManException If the input format is invalid or contains errors.
     */
    public static Task parseTask(String input) throws TinManException {
        assert input != null : "Input cannot be null";
        String trimmedInput = input.trim();
        CommandType commandType = CommandType.parseString(getCommand(trimmedInput));

        switch (commandType) {
        case TODO:
            if (trimmedInput.equals(CommandType.TODO.getKeyword())) {
                throw new TinManException.EmptyDescriptionException("todo");
            }
            return parseTodo(trimmedInput);
        case DEADLINE:
            if (trimmedInput.equals(CommandType.DEADLINE.getKeyword())) {
                throw new TinManException.EmptyDescriptionException("deadline");
            }
            return parseDeadline(trimmedInput);
        case EVENT:
            if (trimmedInput.equals(CommandType.EVENT.getKeyword())) {
                throw new TinManException.EmptyDescriptionException("event");
            }
            return parseEvent(trimmedInput);
        case LIST:
        case MARK:
        case UNMARK:
        case DELETE:
        case BYE:
            throw new TinManException.UnknownCommandException();
        default:
            throw new TinManException.UnknownCommandException();
        }
    }

    /**
     * Extracts the command keyword from user input.
     *
     * @param input The user input string.
     * @return The first word of the input as the command keyword.
     */
    public static String getCommand(String input) {
        return input.trim().split(" ", 2)[0];
    }

    /**
     * Parses the task number from user input for commands that require task indices.
     *
     * @param input The user input string containing the task number.
     * @return Zero-based index of the task.
     * @throws TinManException If the task number is invalid or missing.
     */
    public static int parseTaskNumber(String input) throws TinManException {
        assert input != null : "Input cannot be null";
        try {
            String[] parts = input.split(" ", 2);
            if (parts.length <= 1) {
                throw new TinManException.InvalidTaskNumberException();
            }
            int taskNumber = Integer.parseInt(parts[1]);
            if (taskNumber <= 0) {
                throw new TinManException.InvalidTaskNumberException();
            }
            return taskNumber - 1;
        } catch (NumberFormatException e) {
            throw new TinManException.InvalidTaskNumberException();
        }
    }
}
