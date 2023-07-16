package com.isa.todo.service;

import com.isa.todo.model.Category;
import com.isa.todo.model.Task;
import com.isa.todo.repository.TaskRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private static int successfulTests = 0;
    private static int totalTests = 0;
    Task task1 = new Task("Task 1", Category.WORK, 1, LocalDate.now().plusDays(1));
    Task task2 = new Task("Task 2", Category.HOME, 2, LocalDate.now().plusDays(2));
    Task task3 = new Task("Task 3", Category.OTHER, 3, LocalDate.now().plusDays(3));
    Task task4 = new Task("Task 4", Category.WORK, 1, LocalDate.now().plusDays(1));
    private TaskService taskService;
    private TaskRepository taskRepository;
    private String name;

    @AfterAll
    static void afterAll() {
        System.out.println("AllTests has been completed. Thanks :)\n");
        System.out.println("Successful tests: " + successfulTests + "/" + totalTests);

        if (successfulTests == totalTests) {
            System.out.println("All tests passed successfully!");
        } else {
            System.out.println("Some tests failed.");
        }
    }

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
        when(taskRepository.getAllTasks()).thenReturn(Arrays.asList(task1, task2, task3, task4));

    }

    @AfterEach
    void afterEach() {

        System.out.println(name + " has been completed\n");
    }

    @Test
    void addTask() {

        // When
        taskService.addTask(task1);
        totalTests++;
        // Then
        verify(taskRepository, times(1)).addTask(task1);
        name = "addTask";
        successfulTests++;
    }

    @Test
    void addTask_WhenTaskIsNull_ShouldNotAddTask() {
        // Given
        Task task = null;

        // When
        taskService.addTask(task);
        List<Task> tasks = taskService.getAllTasks();
        totalTests++;
        // Then
        assertFalse(tasks.contains(task));
        name = "addTask_WhenTaskIsNull_ShouldNotAddTask";
        successfulTests++;
    }

    @Test
    void getAllTasks_WhenTasksExist_ShouldReturnAllTasks() {

        // When
        List<Task> tasks = taskService.getAllTasks();
        totalTests++;
        // Then
        assertAll("Tasks",
                () -> assertEquals(4, tasks.size(), "Incorrect number of tasks"),
                () -> assertTrue(tasks.contains(task1), "Task 1 is not present"),
                () -> assertTrue(tasks.contains(task2), "Task 2 is not present"),
                () -> assertTrue(tasks.contains(task3), "Task 3 is not present"),
                () -> assertTrue(tasks.contains(task4), "Task 4 is not present")
        );
        name = "getAllTasks_WhenTasksExist_ShouldReturnAllTasks";
        successfulTests++;
    }

    @Test
    void getAllTasks_WhenNoTasksExist_ShouldReturnEmptyList() {
        // Given
        when(taskRepository.getAllTasks()).thenReturn(Arrays.asList());
        totalTests++;
        // When
        List<Task> tasks = taskService.getAllTasks();

        // Then
        assertTrue(tasks.isEmpty(), "Result is not empty");
        name = "getAllTasks_WhenNoTasksExist_ShouldReturnEmptyList";
        successfulTests++;
    }

    @Test
    void getTaskById() {
        // Given
        when(taskRepository.getTaskById(task1.getId())).thenReturn(task1);
        totalTests++;
        // When
        Task result = taskService.getTaskById(task1.getId());

        // Then
        assertAll("Tasks",
                () -> assertNotNull(result, "Result is null"),
                () -> assertEquals(task1, result, "Result does not match expected task")
        );
        name = "getTaskById";
        successfulTests++;
    }

    @Test
    void getTaskById_WhenTaskDoesNotExist_ShouldReturnNull() {
        // Given
        when(taskRepository.getAllTasks()).thenReturn(Arrays.asList(task1, task2));
        totalTests++;
        // When
        Task result = taskService.getTaskById("nonexistent_id");

        // Then
        assertNull(result, "Result is null");
        name = "getTaskById_WhenTaskDoesNotExist_ShouldReturnNull";

        successfulTests++;
    }

    @Test
    void findTasksWithPriority1_WhenTasksExist_ShouldReturnTasksWithPriority1() {

        // When
        List<Task> tasks = taskService.findTasksWithPriority1();
        totalTests++;
        // Then
        assertAll("Tasks",
                () -> assertEquals(2, tasks.size(), "Incorrect number of tasks"),
                () -> assertTrue(tasks.contains(task1), "Task 1 is not present"),
                () -> assertTrue(tasks.contains(task4), "Task 4 is not present")
        );
        name = "findTasksWithPriority1_WhenTasksExist_ShouldReturnTasksWithPriority1";

        successfulTests++;
    }

    @Test
    void findTasksWithPriority1_WhenNoTasksWithPriority1_ShouldReturnEmptyList() {
        // Given
        totalTests++;
        when(taskRepository.getAllTasks()).thenReturn(Arrays.asList(task2, task3));

        // When
        List<Task> tasks = taskService.findTasksWithPriority1();

        // Then
        assertTrue(tasks.isEmpty(), "Result is not null");
        name = "findTasksWithPriority1_WhenNoTasksWithPriority1_ShouldReturnEmptyList";

        successfulTests++;
    }

    @Test
    void findTasksForNextDay_WhenTasksExist_ShouldReturnTasksForNextDay() {

        // When
        List<Task> tasks = taskService.findTasksForNextDay();
        totalTests++;
        assertAll("Tasks",
                () -> assertEquals(2, tasks.size(), "Incorrect number of tasks"),
                () -> assertTrue(tasks.contains(task1), "Task 1 is not present"),
                () -> assertTrue(tasks.contains(task4), "Task 4 is not present")
        );
        name = "findTasksForNextDay_WhenTasksExist_ShouldReturnTasksForNextDay";

        successfulTests++;
    }

    @Test
    void findTasksForNextDay_WhenNoTasksExist_ShouldReturnEmptyList() {
        // Given
        when(taskRepository.getAllTasks()).thenReturn(Collections.emptyList());
        totalTests++;
        // When
        List<Task> tasks = taskService.findTasksForNextDay();

        // Then
        assertTrue(tasks.isEmpty(), "Result is not null");
        name = "findTasksForNextDay_WhenNoTasksExist_ShouldReturnEmptyList";

        successfulTests++;
    }

    @Test
    void sortTasksByPriorityDescending_WhenTasksExist_ShouldReturnTasksSortedByPriorityDescending() {

        // When
        List<Task> tasks = taskService.sortTasksByPriorityDescending();
        totalTests++;
        // Then
        assertAll("Tasks",
                () -> assertEquals(4, tasks.size(), "Incorrect number of tasks"),
                () -> assertEquals(task1, tasks.get(0), "Task 1 does not match"),
                () -> assertEquals(task4, tasks.get(1), "Task 4 does not match"),
                () -> assertEquals(task2, tasks.get(2), "Task 2 does not match"),
                () -> assertEquals(task3, tasks.get(3), "Task 3 does not match")
        );
        name = "sortTasksByPriorityDescending_WhenTasksExist_ShouldReturnTasksSortedByPriorityDescending";

        successfulTests++;
    }

    @Test
    void sortTasksByPriorityDescending_WhenNoTasksExist_ShouldReturnEmptyList() {
        // Given
        when(taskRepository.getAllTasks()).thenReturn(Collections.emptyList());
        totalTests++;
        // When
        List<Task> tasks = taskService.sortTasksByPriorityDescending();

        // Then
        assertTrue(tasks.isEmpty(), "Result is not null");
        successfulTests++;
    }

    @Test
    void sortTasksByDate_WhenTasksExist_ShouldReturnTasksSortedByDate() {

        // When
        List<Task> tasks = taskService.sortTasksByDate();
        totalTests++;
        // Then
        assertAll("Tasks",
                () -> assertEquals(4, tasks.size(), "Incorrect number of tasks"),
                () -> assertEquals(task1, tasks.get(0), "Task 1 does not match"),
                () -> assertEquals(task4, tasks.get(1), "Task 4 does not match"),
                () -> assertEquals(task2, tasks.get(2), "Task 2 does not match"),
                () -> assertEquals(task3, tasks.get(3), "Task 3 does not match")
        );
        name = "sortTasksByDate_WhenTasksExist_ShouldReturnTasksSortedByDate";

        successfulTests++;
    }

    @Test
    void sortTasksByDate_WhenNoTasksExist_ShouldReturnEmptyList() {
        // Given
        when(taskRepository.getAllTasks()).thenReturn(Collections.emptyList());
        totalTests++;
        // When
        List<Task> tasks = taskService.sortTasksByDate();

        // Then
        assertTrue(tasks.isEmpty(), "Result is not null");
        name = "sortTasksByDate_WhenNoTasksExist_ShouldReturnEmptyList";

        successfulTests++;
    }

    @Test
    void removeTaskById_WhenTaskExists_ShouldRemoveTask() {
        // Given
        String taskId = "task-id";

        totalTests++;
        when(taskRepository.getTaskById(taskId)).thenReturn(task1);

        // When
        taskService.removeTaskById(taskId);

        // Then
        verify(taskRepository, times(1)).removeTaskById(taskId);
        name = "removeTask_WhenTaskExists_ShouldRemoveTask";

        successfulTests++;
    }

    @Test
    void findTasksByCategory_WhenTasksExistForCategory_ShouldReturnTasksForCategory() {

        // When
        List<Task> tasks = taskService.findTasksByCategory(Category.WORK);
        totalTests++;
        // Then
        assertAll("Tasks",
                () -> assertEquals(2, tasks.size(), "Incorrect number of tasks"),
                () -> assertTrue(tasks.contains(task1), "Task 1 is not present"),
                () -> assertTrue(tasks.contains(task4), "Task 4 is not present")
        );
        name = "findTasksByCategory_WhenTasksExistForCategory_ShouldReturnTasksForCategory";

        successfulTests++;
    }

    @Test
    void findTasksByCategory_WhenNoTasksExistForCategory_ShouldReturnEmptyList() {
        // Given
        when(taskRepository.getAllTasks()).thenReturn(Collections.emptyList());
        totalTests++;
        // When
        List<Task> tasks = taskService.findTasksByCategory(Category.HOME);

        // Then
        assertTrue(tasks.isEmpty(), "Result is not empty");
        name = "findTasksByCategory_WhenNoTasksExistForCategory_ShouldReturnEmptyList";

        successfulTests++;
    }

    @Test
    void findTasksByDescriptionContains_WhenTasksExistWithMatchingDescription_ShouldReturnMatchingTasks() {

        // When
        List<Task> tasks = taskService.findTasksByDescriptionContains("Task");
        totalTests++;
        // Then
        assertAll("Tasks",
                () -> assertEquals(4, tasks.size(), "Incorrect number of tasks"),
                () -> assertTrue(tasks.contains(task1), "Task 1 is not present"),
                () -> assertTrue(tasks.contains(task2), "Task 2 is not present"),
                () -> assertTrue(tasks.contains(task3), "Task 3 is not present"),
                () -> assertTrue(tasks.contains(task4), "Task 4 is not present")
        );
        name = "findTasksByDescriptionContains_WhenTasksExistWithMatchingDescription_ShouldReturnMatchingTasks";

        successfulTests++;
    }

    @Test
    void findTasksByDescriptionContains_WhenNoTasksExistWithMatchingDescription_ShouldReturnEmptyList() {

        // When
        List<Task> tasks = taskService.findTasksByDescriptionContains("Work");
        totalTests++;
        // Then
        assertTrue(tasks.isEmpty(), "Result is not empty");
        name = "findTasksByDescriptionContains_WhenNoTasksExistWithMatchingDescription_ShouldReturnEmptyList";

        successfulTests++;
    }

    @Test
    void findMostUrgentTask_WhenTasksExist_ShouldReturnMostUrgentTask() {

        // When
        Optional<Task> mostUrgentTask = taskService.findMostUrgentTask();
        totalTests++;
        // Then
        assertAll("Most Urgent Task",
                () -> assertTrue(mostUrgentTask.isPresent(), "Most urgent task is not present"),
                () -> assertEquals(task1, mostUrgentTask.get(), "Most urgent task does not match expected task")
        );
        name = "findMostUrgentTask_WhenTasksExist_ShouldReturnMostUrgentTask";

        successfulTests++;
    }

    @Test
    void findMostUrgentTask_WhenNoTasksExist_ShouldReturnEmptyOptional() {
        // Given
        when(taskRepository.getAllTasks()).thenReturn(Collections.emptyList());
        totalTests++;
        // When
        Optional<Task> mostUrgentTask = taskService.findMostUrgentTask();

        // Then
        assertFalse(mostUrgentTask.isPresent(), "Most urgent task is present");
        name = "findMostUrgentTask_WhenNoTasksExist_ShouldReturnEmptyOptional";

        successfulTests++;
    }

    @Test
    void divideTasksByCategory_WhenTasksExist_ShouldReturnTasksDividedByCategory() {

        // When
        Map<Category, List<Task>> dividedTasks = taskService.divideTasksByCategory();
        totalTests++;
        // Then
        assertAll("Divided Tasks",
                () -> assertNotNull(dividedTasks, "Divided tasks map is null"),
                () -> assertEquals(3, dividedTasks.size(), "Incorrect number of divided categories"),
                () -> assertTrue(dividedTasks.containsKey(Category.WORK), "Category WORK is not present"),
                () -> assertTrue(dividedTasks.containsKey(Category.HOME), "Category HOME is not present"),
                () -> assertTrue(dividedTasks.containsKey(Category.OTHER), "Category OTHER is not present"),
                () -> assertEquals(Arrays.asList(task1, task4), dividedTasks.get(Category.WORK), "Tasks in WORK category do not match"),
                () -> assertEquals(Collections.singletonList(task2), dividedTasks.get(Category.HOME), "Tasks in HOME category do not match"),
                () -> assertEquals(Collections.singletonList(task3), dividedTasks.get(Category.OTHER), "Tasks in OTHER category do not match")
        );
        name = "divideTasksByCategory_WhenTasksExist_ShouldReturnTasksDividedByCategory";

        successfulTests++;
    }

    @Test
    void divideTasksByCategory_WhenNoTasksExist_ShouldReturnEmptyMap() {
        // Given
        when(taskRepository.getAllTasks()).thenReturn(Collections.emptyList());
        totalTests++;
        // When
        Map<Category, List<Task>> dividedTasks = taskService.divideTasksByCategory();

        // Then
        assertNotNull(dividedTasks, "Divided tasks map is null");
        assertTrue(dividedTasks.isEmpty(), "Divided tasks map is mot empty");
        name = "divideTasksByCategory_WhenNoTasksExist_ShouldReturnEmptyMap";

        successfulTests++;
    }

    @Test
    void divideTasksByPriority_WhenTasksExist_ShouldReturnTasksDividedByPriority() {

        // When
        Map<Integer, List<Task>> dividedTasks = taskService.divideTasksByPriority();
        totalTests++;
        // Then
        assertAll("Divided Tasks",
                () -> assertNotNull(dividedTasks, "Divided tasks map is null"),
                () -> assertEquals(3, dividedTasks.size(), "Incorrect number of divided categories"),
                () -> assertTrue(dividedTasks.containsKey(1), "Category 1 is not present"),
                () -> assertTrue(dividedTasks.containsKey(2), "Category 2 is not present"),
                () -> assertTrue(dividedTasks.containsKey(3), "Category 3 is not present"),
                () -> assertEquals(Arrays.asList(task1, task4), dividedTasks.get(1), "Tasks in Category 1 do not match"),
                () -> assertEquals(Collections.singletonList(task2), dividedTasks.get(2), "Tasks in Category 2 do not match"),
                () -> assertEquals(Collections.singletonList(task3), dividedTasks.get(3), "Tasks in Category 3 do not match")
        );
        name = "divideTasksByPriority_WhenTasksExist_ShouldReturnTasksDividedByPriority";

        successfulTests++;
    }

    @Test
    void divideTasksByPriority_WhenNoTasksExist_ShouldReturnEmptyMap() {
        // Given
        when(taskRepository.getAllTasks()).thenReturn(Collections.emptyList());
        totalTests++;
        // When
        Map<Integer, List<Task>> dividedTasks = taskService.divideTasksByPriority();

        // Then
        assertNotNull(dividedTasks, "Divided tasks map is null");
        assertTrue(dividedTasks.isEmpty(), "Divided tasks map is mot empty");
        name = "divideTasksByPriority_WhenNoTasksExist_ShouldReturnEmptyMap";

        successfulTests++;
    }

    @Test
    void findHighestPriorityTaskForEachCategory_WhenTasksExist_ShouldReturnHighestPriorityTaskForEachCategory() {

        // When
        Map<Category, Optional<Task>> highestPriorityTasks = taskService.findHighestPriorityTaskForEachCategory();
        totalTests++;
        // Then
        assertAll("Highest Priority Tasks",
                () -> assertNotNull(highestPriorityTasks, "Highest priority tasks map is null"),
                () -> assertEquals(3, highestPriorityTasks.size(), "Incorrect number of categories with highest priority tasks"),
                () -> assertTrue(highestPriorityTasks.containsKey(Category.WORK), "Category WORK is not present"),
                () -> assertTrue(highestPriorityTasks.containsKey(Category.HOME), "Category HOME is not present"),
                () -> assertTrue(highestPriorityTasks.containsKey(Category.OTHER), "Category OTHER is not present"),
                () -> assertEquals(task1, highestPriorityTasks.get(Category.WORK).orElse(null), "Highest priority task in WORK category does not match"),
                () -> assertEquals(task2, highestPriorityTasks.get(Category.HOME).orElse(null), "Highest priority task in HOME category does not match"),
                () -> assertEquals(task3, highestPriorityTasks.get(Category.OTHER).orElse(null), "Highest priority task in OTHER category does not match")
        );
        name = "findHighestPriorityTaskForEachCategory_WhenTasksExist_ShouldReturnHighestPriorityTaskForEachCategory";

        successfulTests++;
    }

    @Test
    void findHighestPriorityTaskForEachCategory_WhenNoTasksExist_ShouldReturnEmptyMap() {
        // Given
        when(taskRepository.getAllTasks()).thenReturn(Collections.emptyList());
        totalTests++;
        // When
        Map<Category, Optional<Task>> highestPriorityTasks = taskService.findHighestPriorityTaskForEachCategory();

        // Then
        assertNotNull(highestPriorityTasks, "Divided tasks map is null");
        assertTrue(highestPriorityTasks.isEmpty(), "Divided tasks map is mot empty");
        name = "findHighestPriorityTaskForEachCategory_WhenNoTasksExist_ShouldReturnEmptyMap";

        successfulTests++;

    }
}

