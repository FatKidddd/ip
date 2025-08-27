package tinman.storage;

import tinman.task.Task;
import tinman.exception.TinManException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final String filePath;
    
    public Storage(String filePath) {
        this.filePath = filePath;
    }
    
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
    
    public ArrayList<Task> load() throws TinManException {
        ArrayList<Task> tasks = new ArrayList<>();
        
        File file = new File(filePath);
        if (!file.exists()) {
            return tasks;
        }
        
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    tasks.add(stringToTask(line));
                }
            }
        } catch (IOException e) {
            throw new TinManException("Error loading tasks from file: " + e.getMessage());
        } catch (Exception e) {
            throw new TinManException("Data file is corrupted: " + e.getMessage());
        }
        
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
}