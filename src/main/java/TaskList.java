public class TaskList {
    private static final int MAX_TASKS = 100;
    private Task[] tasks;
    private int taskCount;

    public TaskList() {
        this.tasks = new Task[MAX_TASKS];
        this.taskCount = 0;
    }

    public void addTask(Task task) throws TinManException {
        if (taskCount >= MAX_TASKS) {
            throw new TinManException.TaskListFullException();
        }
        tasks[taskCount++] = task;
    }

    public Task getTask(int index) throws TinManException {
        if (index < 0 || index >= taskCount) {
            throw new TinManException.TaskNotFoundException();
        }
        return tasks[index];
    }

    public int getTaskCount() {
        return taskCount;
    }

    public boolean isValidIndex(int index) {
        return index >= 0 && index < taskCount;
    }

    public String listTasks() {
        if (taskCount == 0) {
            return "Here are the tasks in your list:\n (empty)";
        }
        StringBuilder taskList = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            taskList.append("\n ").append(i + 1).append(".").append(tasks[i]);
        }
        return taskList.toString();
    }
}
