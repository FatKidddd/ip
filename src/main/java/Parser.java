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

    public static Task parseTask(String input) throws TinManException {
        String trimmedInput = input.trim();

        if (trimmedInput.equals("todo")) {
            throw new TinManException.EmptyDescriptionException("todo");
        } else if (trimmedInput.startsWith("todo ")) {
            return parseTodo(trimmedInput);
        } else if (trimmedInput.equals("deadline")) {
            throw new TinManException.EmptyDescriptionException("deadline");
        } else if (trimmedInput.startsWith("deadline ")) {
            return parseDeadline(trimmedInput);
        } else if (trimmedInput.equals("event")) {
            throw new TinManException.EmptyDescriptionException("event");
        } else if (trimmedInput.startsWith("event ")) {
            return parseEvent(trimmedInput);
        } else {
            // Check if it's a known command without arguments
            String command = getCommand(trimmedInput);
            if (command.equals("list") || command.equals("mark") || command.equals("unmark") || command.equals("delete")
                    || command.equals("bye")) {
                throw new TinManException.UnknownCommandException();
            }

            // Unknown command - not a valid todo either
            throw new TinManException.UnknownCommandException();
        }
    }

    public static String getCommand(String input) {
        return input.trim().split(" ", 2)[0];
    }

    public static int parseTaskNumber(String input) throws TinManException {
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
