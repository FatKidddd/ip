package tinman.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tinman.exception.TinManException;
import tinman.task.Task;

/**
 * Handles the loading and saving of task data to and from file storage.
 * Manages file I/O operations for task persistence.
 */
public class Storage {
    private final String filePath;

    /**
     * Constructs a Storage instance with the specified file path.
     *
     * @param filePath Path to the file where tasks will be stored.
     */
    public Storage(String filePath) {
        assert filePath != null : "Precondition: file path cannot be null";
        assert !filePath.trim().isEmpty() : "Precondition: file path cannot be empty";
        this.filePath = filePath;
        assert this.filePath != null : "Class invariant: filePath should never be null after construction";
    }

    /**
     * Saves the list of tasks to the file.
     * Creates the parent directory if it does not exist.
     *
     * @param tasks List of tasks to save.
     * @throws TinManException If there is an error writing to the file.
     */
    public void save(ArrayList<Task> tasks) throws TinManException {
        assert tasks != null : "Precondition: task list cannot be null";
        try {
            ensureDirectoryExists();

            FileWriter writer = new FileWriter(filePath);
            for (Task task : tasks) {
                assert task != null : "Internal invariant: task in list should not be null";
                writer.write(taskToString(task) + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            throw new TinManException("Error saving tasks to file: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the file.
     * Returns an empty list if the file does not exist.
     *
     * @return List of tasks loaded from the file.
     * @throws TinManException If there is an error reading from the file or if the data is corrupted.
     */
    public ArrayList<Task> load() throws TinManException {
        ArrayList<Task> tasks = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            assert tasks.isEmpty() : "Postcondition: should return empty list when file doesn't exist";
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            tasks = lines.stream()
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::parseTaskFromLine)
                    .peek(task -> assert task != null : "Internal invariant: parsed task should not be null")
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            throw new TinManException("Error loading tasks from file: " + e.getMessage());
        } catch (RuntimeException e) {
            if (e.getCause() instanceof TinManException) {
                throw (TinManException) e.getCause();
            }
            throw new TinManException("Data file is corrupted: " + e.getMessage());
        } catch (Exception e) {
            throw new TinManException("Data file is corrupted: " + e.getMessage());
        }

        assert tasks != null : "Postcondition: loaded task list should never be null";
        return tasks;
    }

    private void ensureDirectoryExists() throws IOException {
        Path path = Paths.get(filePath);
        Path directory = path.getParent();
        if (directory != null && !Files.exists(directory)) {
            Files.createDirectories(directory);
        }
    }

    private String taskToString(Task task) {
        return task.toSaveFormat();
    }

    private Task stringToTask(String line) throws TinManException {
        return Saveable.fromSaveFormat(line);
    }

    /**
     * Helper method to parse a task from a line, wrapping checked exceptions
     * for use in streams.
     *
     * @param line The line to parse into a task.
     * @return The parsed task.
     * @throws RuntimeException If parsing fails, wrapping the original TinManException.
     */
    private Task parseTaskFromLine(String line) {
        try {
            return stringToTask(line);
        } catch (TinManException e) {
            throw new RuntimeException("Error parsing task: " + e.getMessage(), e);
        }
    }
}

