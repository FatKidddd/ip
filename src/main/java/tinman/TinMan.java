package tinman;

import tinman.storage.Storage;
import tinman.task.TaskList;
import tinman.task.Task;
import tinman.ui.Ui;
import tinman.parser.Parser;
import tinman.parser.CommandType;
import tinman.exception.TinManException;
import java.util.ArrayList;

public class TinMan {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

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
        CommandType commandType = CommandType.fromString(Parser.getCommand(input));

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

    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            CommandType commandType = CommandType.fromString(Parser.getCommand(input));

            processCommand(input);

            if (commandType == CommandType.BYE) {
                break;
            }
        }

        ui.close();
    }

    public static void main(String[] args) {
        new TinMan("./data/duke.txt").run();
    }
}
