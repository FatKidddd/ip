package tinman.task;

import java.util.ArrayList;

import tinman.exception.TinManException;

/**
 * Represents a collection of tasks that can be managed.
 * Provides operations for adding, deleting, retrieving, and searching tasks.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with the given list of tasks.
     *
     * @param tasks List of tasks to initialize with.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
    }

    /**
     * Adds a task to the task list.
     *
     * @param task Task to be added.
     */
    public void addTask(Task task) {
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
        if (index < 0 || index >= tasks.size()) {
            throw new TinManException.TaskNotFoundException();
        }
        return tasks.get(index);
    }

    /**
     * Deletes and returns a task at the specified index.
     *
     * @param index Zero-based index of the task to delete.
     * @return The deleted task.
     * @throws TinManException If the index is invalid.
     */
    public Task deleteTask(int index) throws TinManException {
        if (index < 0 || index >= tasks.size()) {
            throw new TinManException.TaskNotFoundException();
        }
        return tasks.remove(index);
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public boolean isValidIndex(int index) {
        return index >= 0 && index < tasks.size();
    }

    public String listTasks() {
        if (tasks.isEmpty()) {
            return "Here are the tasks in your list:\n (empty)";
        }
        StringBuilder taskList = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            taskList.append("\n ").append(i + 1).append(".").append(tasks.get(i));
        }
        return taskList.toString();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase().trim();
        
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
