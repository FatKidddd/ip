package tinman;

import java.util.ArrayList;

import tinman.exception.TinManException;
import tinman.parser.CommandType;
import tinman.parser.Parser;
import tinman.storage.Storage;
import tinman.task.Task;
import tinman.task.TaskList;
import tinman.ui.Ui;

/**
 * Represents the TinMan chat bot application.
 * TinMan is a personal task management assistant that helps users manage their tasks,
 * including todos, deadlines, and events.
 */
public class TinMan {
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
        String keyword = input.substring(4).trim(); // Remove "find" and get the keyword
        if (keyword.isEmpty()) {
            ui.showError("Please provide a keyword to search for.");
            return;
        }

        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        ui.showFindResults(matchingTasks);
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
                + "\nNow you have " + remainingCount + " task" + (remainingCount == 1 ? "" : "s") + " in the list.";
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
                + "\nNow you have " + taskCount + " task" + (taskCount == 1 ? "" : "s") + " in the list.";
        } catch (TinManException e) {
            return e.getMessage();
        }
    }

    private String handleFindCommandForGui(String input) {
        String keyword = input.substring(4).trim(); // Remove "find" and get the keyword
        if (keyword.isEmpty()) {
            return "Please provide a keyword to search for.";
        }

        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
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
