package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.service.impl.StateServiceImpl;
import com.softserve.itacademy.service.impl.TaskServiceImpl;
import com.softserve.itacademy.service.impl.ToDoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private TaskServiceImpl taskService;
    private ToDoServiceImpl toDoService;
    private StateServiceImpl stateService;
    @Autowired
    public TaskController(TaskServiceImpl taskService, ToDoServiceImpl toDoService, StateServiceImpl stateService) {
        this.taskService = taskService;
        this.toDoService = toDoService;
        this.stateService = stateService;
    }

    @GetMapping("/create/todos/{todo_id}")
    public String create(@PathVariable(value = "todo_id") long todo_id, Model model) {
        Task task = new Task();
        model.addAttribute("toDoId",todo_id);
        model.addAttribute("task", task);
        return "create-task";
    }

    @PostMapping("/create/todos/{todo_id}")
    public String create(@PathVariable(value = "todo_id") long todo_id, @ModelAttribute Task task) {
        ToDo toDo = toDoService.readById(todo_id);
        task.setTodo(toDo);
        task.setState(stateService.readById(5));
        taskService.create(task);
        return "redirect:/todos/{todo_id}/tasks";
    }

    @GetMapping("/{task_id}/update/todos/{todo_id}")
    public String update(@PathVariable (value = "task_id") long task_id, @PathVariable (value = "todo_id") long todo_id, Model model) {
        Task task = taskService.readById(task_id);
        model.addAttribute("task", task);
        model.addAttribute("states", stateService.getAll());
        return "update-task";
    }

    @PostMapping("/{task_id}/update/todos/{todo_id}")
    public String update(@PathVariable (value = "task_id") long task_id, @PathVariable (value = "todo_id") long todo_id, @ModelAttribute Task task) {
        task.setId(task_id);
        task.setTodo(toDoService.readById(todo_id));
        taskService.update(task);
        return "redirect:/todos/{todo_id}/tasks";
    }

    @GetMapping("/{task_id}/delete/todos/{todo_id}")
    public String delete(@PathVariable (value = "task_id") long task_id, @PathVariable (value = "todo_id") long todo_id) {
        taskService.delete(task_id);
        return "redirect:/todos/{todo_id}/tasks";
    }
}
