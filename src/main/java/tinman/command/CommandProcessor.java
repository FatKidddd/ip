package tinman.command;

import java.util.HashMap;
import java.util.Map;

import tinman.exception.TinManException;
import tinman.parser.CommandType;
import tinman.parser.Parser;
import tinman.task.TaskList;

/**
 * Processes user commands by routing them to appropriate command implementations.
 * Uses the Command Pattern to decouple command parsing from command execution.
 */
public class CommandProcessor {
    private final Map<CommandType, Command> commands;

    /**
     * Creates a CommandProcessor and registers all available commands.
     */
    public CommandProcessor() {
        commands = new HashMap<>();
        registerCommands();
    }

    /**
     * Registers all available commands with their corresponding command types.
     */
    private void registerCommands() {
        commands.put(CommandType.LIST, new ListCommand());
        commands.put(CommandType.MARK, new MarkCommand());
        commands.put(CommandType.UNMARK, new UnmarkCommand());
        commands.put(CommandType.DELETE, new DeleteCommand());
        commands.put(CommandType.FIND, new FindCommand());
        commands.put(CommandType.UPDATE, new UpdateCommand());
        commands.put(CommandType.BYE, new ByeCommand());
        // Task creation commands
        commands.put(CommandType.TODO, new AddTaskCommand());
        commands.put(CommandType.DEADLINE, new AddTaskCommand());
        commands.put(CommandType.EVENT, new AddTaskCommand());
    }

    /**
     * Processes a user input command and returns the result.
     *
     * @param input The user input string.
     * @param tasks The task list to operate on.
     * @return The result message to display to the user.
     * @throws TinManException If command processing fails.
     */
    public String processCommand(String input, TaskList tasks) throws TinManException {
        CommandType commandType = CommandType.parseString(Parser.getCommand(input));
        Command command = commands.get(commandType);
        if (command == null) {
            throw new TinManException.UnknownCommandException();
        }
        return command.execute(tasks, input);
    }
}
