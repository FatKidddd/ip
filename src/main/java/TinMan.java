public class TinMan {
    private TaskList taskList;
    private Ui ui;

    public TinMan() {
        this.taskList = new TaskList();
        this.ui = new Ui();
        loadTasks();
    }

    private void handleListCommand() {
        ui.showTaskList(taskList.listTasks());
    }

    private void handleMarkCommand(String input) {
        try {
            int taskIndex = Parser.parseTaskNumber(input);
            taskList.markTaskDone(taskIndex);
            Task task = taskList.getTask(taskIndex);
            ui.showTaskMarked(task);
        } catch (TinManException e) {
            ui.showError(e.getMessage());
        }
    }

    private void handleUnmarkCommand(String input) {
        try {
            int taskIndex = Parser.parseTaskNumber(input);
            taskList.markTaskNotDone(taskIndex);
            Task task = taskList.getTask(taskIndex);
            ui.showTaskUnmarked(task);
        } catch (TinManException e) {
            ui.showError(e.getMessage());
        }
    }

    private void handleDeleteCommand(String input) {
        try {
            int taskIndex = Parser.parseTaskNumber(input);
            Task deletedTask = taskList.deleteTask(taskIndex);
            ui.showTaskDeleted(deletedTask, taskList.getTaskCount());
        } catch (TinManException e) {
            ui.showError(e.getMessage());
        }
    }

    private void handleAddTaskCommand(String input) {
        try {
            Task task = Parser.parseTask(input);
            taskList.addTask(task);
            ui.showTaskAdded(task, taskList.getTaskCount());
        } catch (TinManException e) {
            ui.showError(e.getMessage());
        }
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

    private void loadTasks() {
        try {
            taskList.loadFromStorage();
        } catch (TinManException e) {
            ui.showError("Warning: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new TinMan().run();
    }
}
