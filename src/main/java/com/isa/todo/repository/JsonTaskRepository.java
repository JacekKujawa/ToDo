package com.isa.todo.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isa.todo.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JsonTaskRepository implements TaskRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRepository.class);
    static Resource resource = new ClassPathResource("tasks.json");
    static File FILE_NAME;

    static {
        try {
            FILE_NAME = resource.getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final ObjectMapper objectMapper;
    private final List<Task> tasks;
    TaskRepository taskRepository;

    public JsonTaskRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.tasks = loadTasksFromFile(objectMapper);
        this.taskRepository = this;
    }

    private static List<Task> loadTasksFromFile(ObjectMapper objectMapper) {
        try {
            File file = new File(FILE_NAME.toURI());
            if (file.exists()) {
                return objectMapper.readValue(file, new TypeReference<>() {
                });
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load tasks from file", e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    @Override
    public void addTask(Task task) {
        tasks.add(task);
        saveTasksToFile();
    }

    @Override
    public Task getTaskById(String id) {
        LOGGER.debug("Searching for task with id: {}", id);

        Optional<Task> taskOptional = this.taskRepository.getAllTasks().stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            LOGGER.debug("Found task: {}", task);
            return task;
        } else {
            LOGGER.debug("Task not found for id: {}", id);
            return null;
        }
    }

    private void saveTasksToFile() {
        try {
            objectMapper.writeValue(new File(FILE_NAME.toURI()), this.tasks);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save tasks to file", e);
        }
    }

    @Override
    public void removeTaskById(String id) {
        Optional<Task> taskOptional = tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();

        taskOptional.ifPresent(task -> {
            tasks.remove(task);
            saveTasksToFile();
        });
    }

}
