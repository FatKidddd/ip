public enum CommandType {
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete"),
    BYE("bye"),
    UNKNOWN("");

    private final String keyword;

    CommandType(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public static CommandType fromString(String command) {
        for (CommandType type : CommandType.values()) {
            if (type.keyword.equals(command.toLowerCase().trim())) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
