package tinman.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import tinman.exception.TinManException;

/**
 * Comprehensive tests for TaskList.deleteTask() method.
 * This method is non-trivial as it handles index validation, task removal,
 * and proper error handling with custom exceptions.
 */
public class TaskListTest {

    private TaskList taskList;
    private Task todo1;
    private Task todo2;
    private Task todo3;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        todo1 = new Todo("Task 1");
        todo2 = new Todo("Task 2");
        todo3 = new Todo("Task 3");
    }

    @Test
    public void deleteTask_validIndex_removesAndReturnsTask() throws TinManException {
        // Setup: Add tasks to list
        taskList.addTask(todo1);
        taskList.addTask(todo2);
        taskList.addTask(todo3);
        assertEquals(3, taskList.getTaskCount());

        // Test: Delete middle task (0-based index)
        Task deletedTask = taskList.deleteTask(1);

        // Verify: Task is removed and returned correctly
        assertEquals(todo2, deletedTask);
        assertEquals(2, taskList.getTaskCount());
        assertEquals(todo1, taskList.getTask(0)); // First task unchanged
        assertEquals(todo3, taskList.getTask(1)); // Third task moved to position 1
    }

    @Test
    public void deleteTask_firstIndex_removesFirstTask() throws TinManException {
        taskList.addTask(todo1);
        taskList.addTask(todo2);
        taskList.addTask(todo3);

        Task deletedTask = taskList.deleteTask(0);

        assertEquals(todo1, deletedTask);
        assertEquals(2, taskList.getTaskCount());
        assertEquals(todo2, taskList.getTask(0)); // Second task moved to first
        assertEquals(todo3, taskList.getTask(1)); // Third task moved to second
    }

    @Test
    public void deleteTask_lastIndex_removesLastTask() throws TinManException {
        taskList.addTask(todo1);
        taskList.addTask(todo2);
        taskList.addTask(todo3);

        Task deletedTask = taskList.deleteTask(2);

        assertEquals(todo3, deletedTask);
        assertEquals(2, taskList.getTaskCount());
        assertEquals(todo1, taskList.getTask(0)); // First task unchanged
        assertEquals(todo2, taskList.getTask(1)); // Second task unchanged
    }

    @Test
    public void deleteTask_singleTask_removesAndLeavesEmpty() throws TinManException {
        taskList.addTask(todo1);
        assertEquals(1, taskList.getTaskCount());

        Task deletedTask = taskList.deleteTask(0);

        assertEquals(todo1, deletedTask);
        assertEquals(0, taskList.getTaskCount());
    }

    @Test
    public void deleteTask_invalidIndexTooHigh_throwsException() {
        taskList.addTask(todo1);
        taskList.addTask(todo2);

        // Try to delete task at index 2 when only 2 tasks exist
        TinManException.TaskNotFoundException exception = 
            assertThrows(TinManException.TaskNotFoundException.class, () -> {
                taskList.deleteTask(2);
            });
        
        // Verify task list is unchanged
        assertEquals(2, taskList.getTaskCount());
    }

    @Test
    public void deleteTask_invalidIndexOutOfBounds_throwsException() {
        taskList.addTask(todo1);

        // Try to delete task at index 1 when only 1 task exists (valid index is 0)
        assertThrows(TinManException.TaskNotFoundException.class, () -> {
            taskList.deleteTask(1);
        });

        // Verify task list is unchanged
        assertEquals(1, taskList.getTaskCount());
    }

    @Test
    public void deleteTask_invalidIndexNegative_throwsException() {
        taskList.addTask(todo1);

        // Try to delete task at negative index
        assertThrows(TinManException.TaskNotFoundException.class, () -> {
            taskList.deleteTask(-1);
        });

        // Verify task list is unchanged
        assertEquals(1, taskList.getTaskCount());
    }

    @Test
    public void deleteTask_emptyList_throwsException() {
        assertEquals(0, taskList.getTaskCount());

        // Try to delete from empty list
        assertThrows(TinManException.TaskNotFoundException.class, () -> {
            taskList.deleteTask(0);
        });

        // Verify list remains empty
        assertEquals(0, taskList.getTaskCount());
    }

    @Test
    public void deleteTask_multipleOperations_maintainsCorrectOrder() throws TinManException {
        // Setup: Add 5 tasks
        Task todo4 = new Todo("Task 4");
        Task todo5 = new Todo("Task 5");
        taskList.addTask(todo1);
        taskList.addTask(todo2);
        taskList.addTask(todo3);
        taskList.addTask(todo4);
        taskList.addTask(todo5);
        assertEquals(5, taskList.getTaskCount());

        // Delete middle task (index 2: Task 3)
        Task deleted1 = taskList.deleteTask(2);
        assertEquals(todo3, deleted1);
        assertEquals(4, taskList.getTaskCount());

        // Delete first task (index 0: Task 1)
        Task deleted2 = taskList.deleteTask(0);
        assertEquals(todo1, deleted2);
        assertEquals(3, taskList.getTaskCount());

        // Verify remaining order: Task 2, Task 4, Task 5
        assertEquals(todo2, taskList.getTask(0));
        assertEquals(todo4, taskList.getTask(1));
        assertEquals(todo5, taskList.getTask(2));
    }

    @Test
    public void deleteTask_withDifferentTaskTypes_handlesCorrectly() throws TinManException {
        // Setup: Add different types of tasks
        Task deadline = new Deadline("Deadline task", "2023-12-25");
        Task event = new Event("Event task", "today", "tomorrow");
        
        taskList.addTask(todo1);
        taskList.addTask(deadline);
        taskList.addTask(event);
        assertEquals(3, taskList.getTaskCount());

        // Delete deadline task
        Task deletedTask = taskList.deleteTask(1);
        assertEquals(deadline, deletedTask);
        assertEquals(2, taskList.getTaskCount());

        // Verify remaining tasks are correct types
        assertEquals(todo1, taskList.getTask(0));
        assertEquals(event, taskList.getTask(1));
    }
}