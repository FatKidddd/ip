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
        this.filePath = filePath;
    }

    /**
     * Saves the list of tasks to the file.
     * Creates the parent directory if it does not exist.
     *
     * @param tasks List of tasks to save.
     * @throws TinManException If there is an error writing to the file.
     */
    public void save(ArrayList<Task> tasks) throws TinManException {
        try {
            ensureDirectoryExists();

            FileWriter writer = new FileWriter(filePath);
            for (Task task : tasks) {
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
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            return lines.stream()
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::parseTaskFromLine)
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

