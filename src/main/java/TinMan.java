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
        printLine();
        printSection("Hello! I'm TinMan\n What can I do for you?");
        printSection("Bye. Hope to see you again soon!");
    }
}
