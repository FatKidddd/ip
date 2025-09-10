package tinman.task;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import tinman.exception.TinManException;

/**
 * Represents a collection of tasks that can be managed.
 * Provides operations for adding, deleting, retrieving, and searching tasks.
 */
public class TaskList {
    private static final int MINIMUM_VALID_INDEX = 0;
    private ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        assert this.tasks != null : "Class invariant: tasks list should never be null";
    }

    /**
     * Constructs a TaskList with the given list of tasks.
     *
     * @param tasks List of tasks to initialize with.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
        assert this.tasks != null : "Class invariant: tasks list should never be null";
    }

    /**
     * Adds a task to the task list.
     *
     * @param task Task to be added.
     */
    public void addTask(Task task) {
        assert task != null : "Cannot add null task to list";
        tasks.add(task);
    }

    /**
     * Retrieves a task at the specified index.
     *
     * @param index Zero-based index of the task.
     * @return Task at the specified index.
     * @throws TinManException If the index is invalid.
     */
    public Task getTask(int index) throws TinManException {
        validateTaskIndex(index);
        Task task = tasks.get(index);
        assert task != null : "Tasks in list should never be null - internal invariant violated";
        return task;
    }

    /**
     * Deletes and returns a task at the specified index.
     *
     * @param index Zero-based index of the task to delete.
     * @return The deleted task.
     * @throws TinManException If the index is invalid.
     */
    public Task deleteTask(int index) throws TinManException {
        validateTaskIndex(index);
        int originalSize = tasks.size();
        Task deletedTask = tasks.remove(index);
        assert deletedTask != null : "Deleted task should never be null - internal invariant violated";
        assert tasks.size() == originalSize - 1 : "List size should decrease by exactly 1 after deletion";
        return deletedTask;
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public boolean isValidIndex(int index) {
        return index >= MINIMUM_VALID_INDEX && index < tasks.size();
    }

    /**
     * Returns a formatted string listing all tasks.
     *
     * @return String representation of all tasks in the list.
     */
    public String listTasks() {
        if (tasks.isEmpty()) {
            return "Here are the tasks in your list:\n (empty)";
        }

        String taskListBody = IntStream.range(0, tasks.size())
                .mapToObj(i -> "\n " + (i + 1) + "." + tasks.get(i))
                .collect(Collectors.joining());

        return "Here are the tasks in your list:" + taskListBody;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Finds tasks that contain the given keyword in their description.
     *
     * @param keyword The keyword to search for in task descriptions.
     * @return List of tasks that match the keyword.
     */
    public ArrayList<Task> findTasks(String keyword) {
        assert keyword != null : "Search keyword cannot be null";
        assert !keyword.trim().isEmpty() : "Search keyword cannot be empty";

        String lowerKeyword = keyword.toLowerCase().trim();

        ArrayList<Task> matchingTasks = tasks.stream()
                .peek(task -> {
                    assert task != null : "Internal invariant: task in list should not be null";
                })
                .filter(task -> task.getDescription().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toCollection(ArrayList::new));

        assert matchingTasks != null : "Postcondition: result should never be null";
        return matchingTasks;
    }

    /**
     * Checks if any task in the list is marked as done.
     *
     * @return true if at least one task is done, false otherwise.
     */
    public boolean hasCompletedTasks() {
        return tasks.stream()
                .anyMatch(Task::getIsDone);
    }

    /**
     * Returns the count of completed tasks using streams.
     *
     * @return Number of completed tasks.
     */
    public long getCompletedTaskCount() {
        return tasks.stream()
                .filter(Task::getIsDone)
                .count();
    }

    /**
     * Validates that the given index is within valid bounds.
     *
     * @param index The index to validate.
     * @throws TinManException If the index is invalid.
     */
    private void validateTaskIndex(int index) throws TinManException {
        if (index < MINIMUM_VALID_INDEX || index >= tasks.size()) {
            throw new TinManException.TaskNotFoundException();
        }
    }
}
