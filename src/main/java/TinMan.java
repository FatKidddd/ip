public class TinMan {
    private TaskList taskList;
    private Ui ui;

    public TinMan() {
        this.taskList = new TaskList();
        this.ui = new Ui();
    }

    private void handleListCommand() {
        ui.showTaskList(taskList.listTasks());
    }

    private void handleMarkCommand(String input) {
        try {
            int taskIndex = Parser.parseTaskNumber(input);
            Task task = taskList.getTask(taskIndex);
            task.markAsDone();
            ui.showTaskMarked(task);
        } catch (TinManException e) {
            ui.showError(e.getMessage());
        }
    }

    private void handleUnmarkCommand(String input) {
        try {
            int taskIndex = Parser.parseTaskNumber(input);
            Task task = taskList.getTask(taskIndex);
            task.markAsNotDone();
            ui.showTaskUnmarked(task);
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
        String command = Parser.getCommand(input);

        if (command.equals("bye")) {
            ui.showGoodbye();
        } else if (command.equals("list")) {
            handleListCommand();
        } else if (command.equals("mark")) {
            handleMarkCommand(input);
        } else if (command.equals("unmark")) {
            handleUnmarkCommand(input);
        } else {
            handleAddTaskCommand(input);
        }
    }

    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            String command = Parser.getCommand(input);

            processCommand(input);

            if (command.equals("bye")) {
                break;
            }
        }

        ui.close();
    }

    public static void main(String[] args) {
        new TinMan().run();
    }
}
