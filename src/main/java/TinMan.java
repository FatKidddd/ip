import java.util.Scanner;

public class TinMan {
    private static final int LINE_LENGTH = 60;
    private static TaskList taskList = new TaskList();

    private static void printLine() {
        System.out.println("_".repeat(LINE_LENGTH));
    }

    private static void printSection(String message) {
        System.out.println(" " + message);
        printLine();
    }

    private static void showWelcome() {
        printLine();
        printSection("""
                        ,----,
                      ,/   .`|                              ____
                    ,`   .'  :                            ,'  , `.
                  ;    ;     / ,--,                    ,-+-,.' _ |
                .'___,/    ,',--.'|         ,---,   ,-+-. ;   , ||                  ,---,
                |    :     | |  |,      ,-+-. /  | ,--.'|'   |  ;|              ,-+-. /  |
                ;    |.';  ; `--'_     ,--.'|'   ||   |  ,', |  ':  ,--.--.    ,--.'|'   |
                `----'  |  | ,' ,'|   |   |  ,"' ||   | /  | |  || /       \\  |   |  ,"' |
                    '   :  ; '  | |   |   | /  | |'   | :  | :  |,.--.  .-. | |   | /  | |
                    |   |  ' |  | :   |   | |  | |;   . |  ; |--'  \\__\\/: . . |   | |  | |
                    '   :  | '  : |__ |   | |  |/ |   : |  | ,     ," .--.; | |   | |  |/
                    ;   |.'  |  | '.'||   | |--'  |   : '  |/     /  /  ,.  | |   | |--'
                    '---'    ;  :    ;|   |/      ;   | |`-'     ;  :   .'   \\|   |/
                             |  ,   / '---'       |   ;/         |  ,     .-./'---'
                              ---`-'              '---'           `--`---'
                """);
        printSection("Hello! I'm TinMan\n What can I do for you?");
    }

    private static void handleListCommand() {
        printSection(taskList.listTasks());
    }

    private static void handleMarkCommand(String input) {
        int taskIndex = Parser.parseTaskNumber(input);
        if (taskIndex != -1 && taskList.isValidIndex(taskIndex)) {
            Task task = taskList.getTask(taskIndex);
            task.markAsDone();
            printSection("Nice! I've marked this task as done:\n  " + task);
        } else {
            printSection("Invalid task number!");
        }
    }

    private static void handleUnmarkCommand(String input) {
        int taskIndex = Parser.parseTaskNumber(input);
        if (taskIndex != -1 && taskList.isValidIndex(taskIndex)) {
            Task task = taskList.getTask(taskIndex);
            task.markAsNotDone();
            printSection("OK, I've marked this task as not done yet:\n  " + task);
        } else {
            printSection("Invalid task number!");
        }
    }

    private static void handleAddTaskCommand(String input) {
        Task task = Parser.parseTask(input);
        if (task != null) {
            taskList.addTask(task);
            int taskCount = taskList.getTaskCount();
            printSection("Got it. I've added this task:\n  " + task +
                    "\nNow you have " + taskCount + " task" + (taskCount == 1 ? "" : "s") + " in the list.");
        } else {
            if (input.startsWith("deadline ")) {
                printSection("Please use format: deadline <description> /by <time>");
            } else if (input.startsWith("event ")) {
                printSection("Please use format: event <description> /from <start> /to <end>");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        showWelcome();

        while (true) {
            String input = scanner.nextLine();
            String command = Parser.getCommand(input);

            if (command.equals("bye")) {
                printSection("Bye. Hope to see you again soon!");
                scanner.close();
                return;
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
    }
}
