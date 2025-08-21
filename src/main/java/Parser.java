public class Parser {
    public static Task parseTask(String input) {
        if (input.startsWith("todo ")) {
            String description = input.substring(5);
            return new Todo(description);
        } else if (input.startsWith("deadline ")) {
            String fullCommand = input.substring(9);
            int byIndex = fullCommand.indexOf(" /by ");
            if (byIndex != -1) {
                String description = fullCommand.substring(0, byIndex);
                String by = fullCommand.substring(byIndex + 5);
                return new Deadline(description, by);
            }
        } else if (input.startsWith("event ")) {
            String fullCommand = input.substring(6);
            int fromIndex = fullCommand.indexOf(" /from ");
            int toIndex = fullCommand.indexOf(" /to ");
            if (fromIndex != -1 && toIndex != -1) {
                String description = fullCommand.substring(0, fromIndex);
                String from = fullCommand.substring(fromIndex + 7, toIndex);
                String to = fullCommand.substring(toIndex + 5);
                return new Event(description, from, to);
            }
        }
        return null;
    }

    public static String getCommand(String input) {
        String[] parts = input.split(" ", 2);
        return parts[0];
    }

    public static int parseTaskNumber(String input) {
        try {
            String[] parts = input.split(" ", 2);
            if (parts.length > 1) {
                return Integer.parseInt(parts[1]) - 1;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
        return -1;
    }
}
