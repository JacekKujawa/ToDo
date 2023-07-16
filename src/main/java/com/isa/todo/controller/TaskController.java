package com.isa.todo.controller;

import com.isa.todo.model.Category;
import com.isa.todo.model.Task;
import com.isa.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;

    }

    @GetMapping("/")
    public String home(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        model.addAttribute("pageTitle", "Tasks list");
        return "index";
    }

    @GetMapping("/new")
    public String showNewTaskForm(Model model) {
        model.addAttribute("newTask", new Task());
        return "new";
    }

    @PostMapping("/new")
    public String addTask(@ModelAttribute("newTask") @Valid Task task, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "new";
        }
        taskService.addTask(task);
        redirectAttributes.addAttribute("successMessage", "Task added successfully!");
        return "redirect:/";
    }

    @GetMapping("/remove/{id}")
    public String deleteTask(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        Task task = taskService.getTaskById(id);
        if (task != null) {
            taskService.removeTaskById(id);
        }
        redirectAttributes.addAttribute("successMessage", "Task remove successfully!");
        return "redirect:/";
    }

    @GetMapping("/priority1")
    public String getTasksWithPriority1(Model model) {
        List<Task> priority1Tasks = taskService.findTasksWithPriority1();
        model.addAttribute("tasks", priority1Tasks);
        model.addAttribute("pageTitle", "Priority 1 Tasks");
        if (priority1Tasks.isEmpty()) {
            model.addAttribute("Message", "No Priority 1 tasks found.");
        }
        return "index";
    }

    @GetMapping("/next-day")
    public String getNextDayTasks(Model model) {
        List<Task> nextDayTasks = taskService.findTasksForNextDay();
        model.addAttribute("tasks", nextDayTasks);
        model.addAttribute("pageTitle", "Next Day Tasks");
        if (nextDayTasks.isEmpty()) {
            model.addAttribute("Message", "No next day tasks found.");
        }
        return "index";
    }

    @GetMapping("/priority-desc")
    public String sortTasksByPriorityDescending(Model model) {
        List<Task> priorityTasks = taskService.sortTasksByPriorityDescending();
        model.addAttribute("tasks", priorityTasks);
        model.addAttribute("pageTitle", "Priority descending tasks");
        if (priorityTasks.isEmpty()) {
            model.addAttribute("Message", "No tasks found.");
        }
        return "index";
    }


    @GetMapping("/sorted-set")
    public String sortTasksByDate(Model model) {
        List<Task> sortTasks = taskService.sortTasksByDate();
        model.addAttribute("tasks", sortTasks);
        model.addAttribute("pageTitle", "Sorted tasks by date");
        if (sortTasks.isEmpty()) {
            model.addAttribute("Message", "No tasks found.");
        }
        return "index";
    }

    @GetMapping("/category")
    public String getTasksByCategory(Model model, @RequestParam("category") Category category) {
        List<Task> taskByCategory = taskService.findTasksByCategory(category);
        model.addAttribute("tasks", taskByCategory);
        model.addAttribute("pageTitle", "Tasks by category");
        if (taskByCategory.isEmpty()) {
            model.addAttribute("Message", "No tasks found.");
        }
        return "index";
    }

    @GetMapping("/description")
    public String getTasksByDescriptionContains(Model model, @RequestParam("search") String keyword) {
        List<Task> taskByByDescriptionContains = taskService.findTasksByDescriptionContains(keyword);
        model.addAttribute("tasks", taskByByDescriptionContains);
        model.addAttribute("pageTitle", "Tasks by description contains");
        if (taskByByDescriptionContains.isEmpty()) {
            model.addAttribute("Message", "No tasks found.");
        }
        return "index";
    }

    @GetMapping("/most-urgent")
    public String getMostUrgentTask(Model model) {
        Optional<Task> taskOptional = taskService.findMostUrgentTask();
        if (taskOptional.isPresent()) {
            model.addAttribute("tasks", Collections.singletonList(taskOptional.get()));
        } else {
            model.addAttribute("tasks", new ArrayList<>());
        }
        model.addAttribute("pageTitle", "Most urgent task");
        if (taskOptional.isEmpty()) {
            model.addAttribute("Message", "No tasks found.");
        }
        return "index";
    }

    @GetMapping("/by-category")
    public String getTasksDividedByCategory(Model model) {
        Map<Category, List<Task>> tasksByCategory = taskService.divideTasksByCategory();
        model.addAttribute("tasks", tasksByCategory);
        model.addAttribute("pageTitle", "Tasks Divided by Category");
        return "map";
    }

    @GetMapping("/by-priority")
    public String getTasksDividedByPriority(Model model) {
        Map<Integer, List<Task>> tasksByPriority = taskService.divideTasksByPriority();
        model.addAttribute("tasks", tasksByPriority);
        model.addAttribute("pageTitle", "Tasks Divided by Priority");
        return "map";
    }

    @GetMapping("/highest-priority")
    public String findHighestPriorityTaskForEachCategory(Model model) {
        Map<Category, Optional<Task>> highestPriorityTasks = taskService.findHighestPriorityTaskForEachCategory();
        model.addAttribute("tasks", highestPriorityTasks);
        model.addAttribute("pageTitle", "Highest Priority Task for Each Category");
        return "map2";
    }

    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }

}