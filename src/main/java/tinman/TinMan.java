package tinman;

import tinman.command.CommandProcessor;
import tinman.exception.TinManException;
import tinman.parser.CommandType;
import tinman.parser.Parser;
import tinman.storage.Storage;
import tinman.task.TaskList;
import tinman.ui.Ui;

/**
 * Represents the TinMan chat bot application.
 * TinMan is a personal task management assistant that helps users manage their tasks,
 * including todos, deadlines, and events.
 * 
 * This version uses the Command Pattern for cleaner separation of concerns.
 */
public class TinMan {
    private final CommandProcessor commandProcessor;
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates a TinMan instance with the specified file path for data storage.
     * Initializes the UI, storage, and loads existing tasks from the file.
     * If loading fails, starts with an empty task list.
     *
     * @param filePath Path to the file where tasks are stored.
     */
    public TinMan(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.commandProcessor = new CommandProcessor();
        
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (TinManException e) {
            ui.showError("Warning: " + e.getMessage());
            loadedTasks = new TaskList();
        }
        this.tasks = loadedTasks;
    }

    /**
     * Processes a command for the GUI and returns the response as a string.
     *
     * @param input The user input command.
     * @return The response string to display in the GUI.
     */
    public String processCommandForGui(String input) {
        return processInput(input);
    }

    /**
     * Processes a user input command and returns the result.
     * Handles storage saving and error handling centrally.
     *
     * @param input The user input string.
     * @return The result message to display to the user.
     */
    private String processInput(String input) {
        try {
            String result = commandProcessor.processCommand(input, tasks);
            
            // Save to storage after successful command execution
            // (except for commands that don't modify data)
            CommandType commandType = CommandType.parseString(Parser.getCommand(input));
            if (shouldSaveAfterCommand(commandType)) {
                storage.save(tasks.getTasks());
            }
            
            return result;
        } catch (TinManException e) {
            return e.getMessage();
        }
    }

    /**
     * Determines if the task list should be saved after executing a command.
     *
     * @param commandType The type of command that was executed.
     * @return True if the command modifies data and requires saving.
     */
    private boolean shouldSaveAfterCommand(CommandType commandType) {
        return commandType == CommandType.TODO
                || commandType == CommandType.DEADLINE
                || commandType == CommandType.EVENT
                || commandType == CommandType.MARK
                || commandType == CommandType.UNMARK
                || commandType == CommandType.DELETE
                || commandType == CommandType.UPDATE;
    }

    /**
     * Runs the main application loop for CLI mode.
     * Shows welcome message, processes user commands until bye command is received,
     * then closes the UI resources.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            CommandType commandType = CommandType.parseString(Parser.getCommand(input));

            String result = processInput(input);
            ui.showMessage(result);

            if (commandType == CommandType.BYE) {
                break;
            }
        }

        ui.close();
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