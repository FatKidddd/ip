import java.util.Scanner;

class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}

public class TinMan {
    private static final int LINE_LENGTH = 60;

    private static void printLine() {
        System.out.println("_".repeat(LINE_LENGTH));
    }

    private static void printSection(String message) {
        System.out.println(" " + message);
        printLine();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
        Task[] tasks = new Task[100];
        int taskCount = 0;

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

        while (true) {
            input = scanner.nextLine();

            if (input.equals("bye")) {
                printSection("Bye. Hope to see you again soon!");
                break;
            } else if (input.equals("list")) {
                StringBuilder taskList = new StringBuilder("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    taskList.append("\n ").append(i + 1).append(".").append(tasks[i]);
                }
                printSection(taskList.toString());
            } else if (input.startsWith("mark ")) {
                try {
                    int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                    if (taskIndex >= 0 && taskIndex < taskCount) {
                        tasks[taskIndex].markAsDone();
                        printSection("Nice! I've marked this task as done:\n  " + tasks[taskIndex]);
                    } else {
                        printSection("Invalid task number!");
                    }
                } catch (NumberFormatException e) {
                    printSection("Please provide a valid task number!");
                }
            } else if (input.startsWith("unmark ")) {
                try {
                    int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                    if (taskIndex >= 0 && taskIndex < taskCount) {
                        tasks[taskIndex].markAsNotDone();
                        printSection("OK, I've marked this task as not done yet:\n  " + tasks[taskIndex]);
                    } else {
                        printSection("Invalid task number!");
                    }
                } catch (NumberFormatException e) {
                    printSection("Please provide a valid task number!");
                }
            } else {
                tasks[taskCount++] = new Task(input);
                printSection("added: " + input);
            }
        }

        scanner.close();
    }
}
