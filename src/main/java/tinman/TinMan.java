package tinman;

import java.util.ArrayList;

import tinman.exception.TinManException;
import tinman.parser.CommandType;
import tinman.parser.Parser;
import tinman.storage.Storage;
import tinman.task.Deadline;
import tinman.task.Event;
import tinman.task.Task;
import tinman.task.TaskList;
import tinman.ui.Ui;

/**
 * Represents the TinMan chat bot application.
 * TinMan is a personal task management assistant that helps users manage their tasks,
 * including todos, deadlines, and events.
 */
public class TinMan {
    private static final int FIND_COMMAND_LENGTH = 4;
    private static final int SINGULAR_TASK_COUNT = 1;
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Creates a TinMan instance with the specified file path for data storage.
     * Initializes the UI, storage, and loads existing tasks from the file.
     * If loading fails, starts with an empty task list.
     *
     * @param filePath Path to the file where tasks are stored.
     */
    public TinMan(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (TinManException e) {
            ui.showError("Warning: " + e.getMessage());
            tasks = new TaskList();
        }
    }

    private void handleListCommand() {
        ui.showTaskList(tasks.listTasks());
    }

    private String handleListCommandForGui() {
        return tasks.listTasks();
    }

    private void handleMarkCommand(String input) {
        try {
            int taskIndex = Parser.parseTaskNumber(input);
            Task task = tasks.getTask(taskIndex);
            task.markAsDone();
            storage.save(tasks.getTasks());
            ui.showTaskMarked(task);
        } catch (TinManException e) {
            ui.showError(e.getMessage());
        }
    }

    private String handleMarkCommandForGui(String input) {
        try {
            int taskIndex = Parser.parseTaskNumber(input);
            Task task = tasks.getTask(taskIndex);
            task.markAsDone();
            storage.save(tasks.getTasks());
            return "Nice! I've marked this task as done:\n  " + task;
        } catch (TinManException e) {
            return e.getMessage();
        }
    }

    private void handleUnmarkCommand(String input) {
        try {
            int taskIndex = Parser.parseTaskNumber(input);
            Task task = tasks.getTask(taskIndex);
            task.markAsNotDone();
            storage.save(tasks.getTasks());
            ui.showTaskUnmarked(task);
        } catch (TinManException e) {
            ui.showError(e.getMessage());
        }
    }

    private void handleDeleteCommand(String input) {
        try {
            int taskIndex = Parser.parseTaskNumber(input);
            Task deletedTask = tasks.deleteTask(taskIndex);
            storage.save(tasks.getTasks());
            ui.showTaskDeleted(deletedTask, tasks.getTaskCount());
        } catch (TinManException e) {
            ui.showError(e.getMessage());
        }
    }

    private void handleAddTaskCommand(String input) {
        try {
            Task task = Parser.parseTask(input);
            tasks.addTask(task);
            storage.save(tasks.getTasks());
            ui.showTaskAdded(task, tasks.getTaskCount());
        } catch (TinManException e) {
            ui.showError(e.getMessage());
        }
    }

    private void handleFindCommand(String input) {
        String keyword = extractSearchKeyword(input);
        if (keyword.isEmpty()) {
            ui.showError("Please provide a keyword to search for.");
            return;
        }

        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        ui.showFindResults(matchingTasks);
    }

    private void handleUpdateCommand(String input) {
        try {
            String[] updateParts = Parser.parseUpdateCommand(input);
            int taskIndex = Integer.parseInt(updateParts[0]) - 1;
            String parameters = updateParts[1];

            Task task = tasks.getTask(taskIndex);
            processUpdateParameters(task, parameters);

            storage.save(tasks.getTasks());
            ui.showTaskUpdated(task);
        } catch (NumberFormatException e) {
            ui.showError("Invalid task number format.");
        } catch (TinManException e) {
            ui.showError(e.getMessage());
        }
    }

    private void processUpdateParameters(Task task, String parameters) throws TinManException {
        if (parameters.startsWith("/desc ")) {
            String newDescription = parameters.substring(6).trim();
            if (newDescription.isEmpty()) {
                throw new TinManException("Description cannot be empty.");
            }
            task.updateDescription(newDescription);
        } else if (parameters.startsWith("/by ") && task instanceof Deadline) {
            String newDeadline = parameters.substring(4).trim();
            if (newDeadline.isEmpty()) {
                throw new TinManException("Deadline cannot be empty.");
            }
            Deadline deadline = (Deadline) task;
            deadline.updateDeadline(newDeadline);
        } else if (parameters.contains("/from ") && parameters.contains("/to ") && task instanceof Event) {
            // Handle event updates with optional description
            Event event = (Event) task;

            if (parameters.startsWith("/from ")) {
                // Only updating times, no description
                String[] eventParts = Parser.extractParts(
                        parameters, " /from ", "event", "/from <start> /to <end>");
                String[] toParts = Parser.extractParts(
                        eventParts[1], " /to ", "event", "/from <start> /to <end>");

                String newFrom = toParts[0].trim();
                String newTo = toParts[1].trim();

                if (newFrom.isEmpty() || newTo.isEmpty()) {
                    throw new TinManException("Event times cannot be empty.");
                }

                event.updateFrom(newFrom);
                event.updateTo(newTo);
            } else {
                // Updating description and times
                String[] descParts = Parser.extractParts(
                        parameters, " /from ", "event", "/desc <description> /from <start> /to <end>");
                String newDescription = descParts[0].trim();
                String timesPart = descParts[1];

                String[] toParts = Parser.extractParts(
                        timesPart, " /to ", "event", "/from <start> /to <end>");

                String newFrom = toParts[0].trim();
                String newTo = toParts[1].trim();

                if (newDescription.isEmpty() || newFrom.isEmpty() || newTo.isEmpty()) {
                    throw new TinManException("Description and event times cannot be empty.");
                }

                event.updateDescription(newDescription);
                event.updateFrom(newFrom);
                event.updateTo(newTo);
            }
        } else {
            String taskType = task instanceof Deadline ? "deadline"
                    : task instanceof Event ? "event" : "todo";
            String availableOptions;
            if (taskType.equals("deadline")) {
                availableOptions = "/desc <new description> or /by <new date>";
            } else if (taskType.equals("event")) {
                availableOptions = "/desc <new description>, /from <start> /to <end>, "
                        + "or <description> /from <start> /to <end>";
            } else {
                availableOptions = "/desc <new description>";
            }
            throw new TinManException("Invalid update parameters for " + taskType + ". Available formats: "
                    + availableOptions);
        }
    }

    private void processCommand(String input) {
        CommandType commandType = CommandType.parseString(Parser.getCommand(input));

        switch (commandType) {
        case BYE:
            ui.showGoodbye();
            break;
        case LIST:
            handleListCommand();
            break;
        case MARK:
            handleMarkCommand(input);
            break;
        case UNMARK:
            handleUnmarkCommand(input);
            break;
        case DELETE:
            handleDeleteCommand(input);
            break;
        case FIND:
            handleFindCommand(input);
            break;
        case UPDATE:
            handleUpdateCommand(input);
            break;
        default:
            handleAddTaskCommand(input);
            break;
        }
    }

    /**
     * Runs the main application loop.
     * Shows welcome message, processes user commands until bye command is received,
     * then closes the UI resources.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            CommandType commandType = CommandType.parseString(Parser.getCommand(input));

            processCommand(input);

            if (commandType == CommandType.BYE) {
                break;
            }
        }

        ui.close();
    }

    /**
     * Processes a command for the GUI and returns the response as a string.
     *
     * @param input The user input command.
     * @return The response string to display in the GUI.
     */
    public String processCommandForGui(String input) {
        CommandType commandType = CommandType.parseString(Parser.getCommand(input));

        switch (commandType) {
        case BYE:
            return "Bye. Hope to see you again soon!";
        case LIST:
            return handleListCommandForGui();
        case MARK:
            return handleMarkCommandForGui(input);
        case UNMARK:
            return handleUnmarkCommandForGui(input);
        case DELETE:
            return handleDeleteCommandForGui(input);
        case FIND:
            return handleFindCommandForGui(input);
        case UPDATE:
            return handleUpdateCommandForGui(input);
        default:
            return handleAddTaskCommandForGui(input);
        }
    }

    private String handleUnmarkCommandForGui(String input) {
        try {
            int taskIndex = Parser.parseTaskNumber(input);
            Task task = tasks.getTask(taskIndex);
            task.markAsNotDone();
            storage.save(tasks.getTasks());
            return "OK, I've marked this task as not done yet:\n  " + task;
        } catch (TinManException e) {
            return e.getMessage();
        }
    }

    private String handleDeleteCommandForGui(String input) {
        try {
            int taskIndex = Parser.parseTaskNumber(input);
            Task deletedTask = tasks.deleteTask(taskIndex);
            storage.save(tasks.getTasks());
            int remainingCount = tasks.getTaskCount();
            return "Noted. I've removed this task:\n  " + deletedTask
                + "\nNow you have " + formatTaskCountMessage(remainingCount) + " in the list.";
        } catch (TinManException e) {
            return e.getMessage();
        }
    }

    private String handleAddTaskCommandForGui(String input) {
        try {
            Task task = Parser.parseTask(input);
            tasks.addTask(task);
            storage.save(tasks.getTasks());
            int taskCount = tasks.getTaskCount();
            return "Got it. I've added this task:\n  " + task
                + "\nNow you have " + formatTaskCountMessage(taskCount) + " in the list.";
        } catch (TinManException e) {
            return e.getMessage();
        }
    }

    private String handleFindCommandForGui(String input) {
        String keyword = extractSearchKeyword(input);
        if (keyword.isEmpty()) {
            return "Please provide a keyword to search for.";
        }

        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        return formatFindResults(matchingTasks);
    }

    private String handleUpdateCommandForGui(String input) {
        try {
            String[] updateParts = Parser.parseUpdateCommand(input);
            int taskIndex = Integer.parseInt(updateParts[0]) - 1;
            String parameters = updateParts[1];

            Task task = tasks.getTask(taskIndex);
            processUpdateParameters(task, parameters);

            storage.save(tasks.getTasks());
            return "Got it! I've updated this task:\n  " + task;
        } catch (NumberFormatException e) {
            return "Invalid task number format.";
        } catch (TinManException e) {
            return e.getMessage();
        }
    }

    /**
     * Extracts the search keyword from the find command input.
     * Removes the "find" prefix and trims whitespace.
     *
     * @param input The full find command input.
     * @return The search keyword.
     */
    private String extractSearchKeyword(String input) {
        return input.substring(FIND_COMMAND_LENGTH).trim();
    }

    /**
     * Formats the task count message with proper singular/plural form.
     *
     * @param count The number of tasks.
     * @return Formatted task count message.
     */
    private String formatTaskCountMessage(int count) {
        String taskWord = (count == SINGULAR_TASK_COUNT) ? "task" : "tasks";
        return count + " " + taskWord;
    }

    /**
     * Formats the find results for display in GUI.
     *
     * @param matchingTasks List of tasks that match the search criteria.
     * @return Formatted find results string.
     */
    private String formatFindResults(ArrayList<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            return "Here are the matching tasks in your list:\n (no matching tasks found)";
        }

        StringBuilder result = new StringBuilder("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            result.append("\n ").append(i + 1).append(".").append(matchingTasks.get(i));
        }
        return result.toString();
    }

    /**
     * Starts the TinMan application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new TinMan("./data/tinman.txt").run();
    }
}
