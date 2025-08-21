import java.util.Scanner;

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
            } else {
                printSection(input);
            }
        }

        scanner.close();
    }
}
