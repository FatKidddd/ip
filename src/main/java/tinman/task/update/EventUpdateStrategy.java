package tinman.task.update;

import tinman.exception.TinManException;
import tinman.parser.Parser;
import tinman.task.Event;
import tinman.task.Task;

/**
 * Update strategy for Event tasks.
 * Supports updating description, event times, or both.
 */
public class EventUpdateStrategy implements UpdateStrategy {
    @Override
    public boolean canHandle(String parameters) {
        return parameters.startsWith("/desc ") 
                || parameters.startsWith("/from ")
                || hasFromToPattern(parameters);
    }

    @Override
    public void update(Task task, String parameters) throws TinManException {
        Event event = (Event) task;

        if (parameters.startsWith("/desc ")) {
            String newDescription = parameters.substring(6).trim();
            if (newDescription.isEmpty()) {
                throw new TinManException("Description cannot be empty.");
            }
            event.updateDescription(newDescription);
        } else if (parameters.startsWith("/from ")) {
            // Only updating times: "/from time /to time"
            updateEventTimes(event, parameters);
        } else {
            // Must be description + times format since canHandle() validated it
            updateDescriptionAndTimes(event, parameters);
        }
    }

    /**
     * Checks if parameters contain the /from and /to pattern in the right positions.
     */
    private boolean hasFromToPattern(String parameters) {
        int fromIndex = parameters.indexOf(" /from ");
        int toIndex = parameters.indexOf(" /to ");
        return fromIndex > 0 && toIndex > fromIndex;
    }

    /**
     * Updates only event times from /from /to format.
     */
    private void updateEventTimes(Event event, String parameters) throws TinManException {
        String[] fromParts = Parser.extractParts(parameters, " /from ", "event", "/from <start> /to <end>");
        String[] toParts = Parser.extractParts(fromParts[1], " /to ", "event", "/from <start> /to <end>");

        String newFrom = toParts[0].trim();
        String newTo = toParts[1].trim();

        if (newFrom.isEmpty() || newTo.isEmpty()) {
            throw new TinManException("Event times cannot be empty.");
        }

        event.updateFrom(newFrom);
        event.updateTo(newTo);
    }

    /**
     * Updates both description and event times from "description /from time /to time" format.
     */
    private void updateDescriptionAndTimes(Event event, String parameters) throws TinManException {
        String[] fromParts = Parser.extractParts(parameters, " /from ", "event", "<description> /from <start> /to <end>");
        String description = fromParts[0].trim();
        String[] toParts = Parser.extractParts(fromParts[1], " /to ", "event", "/from <start> /to <end>");

        String newFrom = toParts[0].trim();
        String newTo = toParts[1].trim();

        if (description.isEmpty() || newFrom.isEmpty() || newTo.isEmpty()) {
            throw new TinManException("Description and event times cannot be empty.");
        }

        event.updateDescription(description);
        event.updateFrom(newFrom);
        event.updateTo(newTo);
    }

    @Override
    public String getAvailableOptions() {
        return "/desc <new description>, /from <start> /to <end>, "
                + "or <description> /from <start> /to <end>";
    }
}