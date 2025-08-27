import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;
    private Storage storage;

    public TaskList() {
        this.tasks = new ArrayList<>();
        this.storage = new Storage("./data/duke.txt");
    }

    public TaskList(Storage storage) {
        this.tasks = new ArrayList<>();
        this.storage = storage;
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveToStorage();
    }

    public Task getTask(int index) throws TinManException {
        if (index < 0 || index >= tasks.size()) {
            throw new TinManException.TaskNotFoundException();
        }
        return tasks.get(index);
    }

    public Task deleteTask(int index) throws TinManException {
        if (index < 0 || index >= tasks.size()) {
            throw new TinManException.TaskNotFoundException();
        }
        Task deletedTask = tasks.remove(index);
        saveToStorage();
        return deletedTask;
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

    public void loadFromStorage() throws TinManException {
        this.tasks = storage.load();
    }

    private void saveToStorage() {
        try {
            storage.save(tasks);
        } catch (TinManException e) {
            // Ignore save errors to prevent disrupting normal operation
            System.err.println("Warning: Failed to save tasks - " + e.getMessage());
        }
    }

    public void markTaskDone(int index) throws TinManException {
        Task task = getTask(index);
        task.markAsDone();
        saveToStorage();
    }

    public void markTaskNotDone(int index) throws TinManException {
        Task task = getTask(index);
        task.markAsNotDone();
        saveToStorage();
    }
}
