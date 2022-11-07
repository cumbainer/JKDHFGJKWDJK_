package com.softserve.itacademy.controller.Rest;

import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tasks/")
public class TaskRestController {

    private TaskService taskService;
    private StateService stateService;
    private ToDoService toDoService;

    @Autowired
    public TaskRestController(TaskService taskService, StateService stateService, ToDoService toDoService) {
        this.taskService = taskService;
        this.stateService = stateService;
        this.toDoService = toDoService;
    }

    @GetMapping("")
    public ResponseEntity<List<Task>> getAll(){
        List<Task> tasks = this.taskService.getAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Task> getOneTask(@PathVariable("id") Long taskId){
        if (taskId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Task task = taskService.readById(taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable("id") Long taskId) {
        Task task = taskService.readById(taskId);
        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        taskService.delete(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("") ///????????????????????????????
    public ResponseEntity<Task> createTask(@RequestBody Task task, Long todoId) {
        if (task == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ToDo toDo = toDoService.readById(todoId);
        task.setTodo(toDo);
        task.setState(stateService.readById(5));
        taskService.create(task);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<Task> updateTask(@RequestBody @Valid Task task){
        taskService.update(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}
