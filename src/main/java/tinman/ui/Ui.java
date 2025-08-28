package tinman.ui;

import tinman.task.Task;
import java.util.Scanner;

/**
 * Handles all user interface operations.
 * Manages user input and output display for the TinMan application.
 */
public class Ui {
    private static final int LINE_LENGTH = 60;
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showLine() {
        System.out.println("_".repeat(LINE_LENGTH));
    }

    public void showMessage(String message) {
        System.out.println(message);
        showLine();
    }

    /**
     * Displays the welcome message with ASCII art logo and greeting.
     */
    public void showWelcome() {
        showLine();
        showMessage("""
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
        showMessage("Hello! I'm TinMan\nWhat can I do for you?");
    }

    public void showGoodbye() {
        showMessage("Bye. Hope to see you again soon!");
    }

    public void showError(String errorMessage) {
        showMessage(errorMessage);
    }

    public void showTaskAdded(Task task, int taskCount) {
        showMessage("Got it. I've added this task:\n  " + task +
                "\nNow you have " + taskCount + " task" + (taskCount == 1 ? "" : "s") + " in the list.");
    }

    public void showTaskMarked(Task task) {
        showMessage("Nice! I've marked this task as done:\n  " + task);
    }

    public void showTaskUnmarked(Task task) {
        showMessage("OK, I've marked this task as not done yet:\n  " + task);
    }

    public void showTaskDeleted(Task task, int remainingCount) {
        showMessage("Noted. I've removed this task:\n  " + task +
                "\nNow you have " + remainingCount + " task" + (remainingCount == 1 ? "" : "s") + " in the list.");
    }

    public void showTaskList(String taskList) {
        showMessage(taskList);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}
