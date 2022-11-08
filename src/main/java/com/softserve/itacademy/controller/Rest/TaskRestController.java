package com.softserve.itacademy.controller.Rest;

import com.softserve.itacademy.dto.TaskDto;
import com.softserve.itacademy.dto.TaskTransformer;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.service.StateService;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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

    @PostMapping("/todo/{todo_id}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public /*Map<String, String>*/ ResponseEntity create(@PathVariable String todo_id, @RequestBody Map<String, String> task) {
        try {
            TaskDto taskDto = new TaskDto(Long.parseLong(task.get("task_id")), task.get("task"), task.get("priority"), Long.parseLong(task.get("todo_id")), Long.parseLong(task.get("state_id")));
            Task task1 = TaskTransformer.convertToEntity(taskDto,
                    toDoService.readById(taskDto.getTodoId()),
                    stateService.getByName("New"));
            return getTaskMap(taskService.create(task1),HttpStatus.CREATED);
        }
        catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable String id, @RequestBody Map<String, String> editedMap) {
        try {
            TaskDto taskDto = new TaskDto(Long.parseLong(editedMap.get("task_id")), editedMap.get("task"), editedMap.get("priority"), Long.parseLong(editedMap.get("todo_id")), Long.parseLong(editedMap.get("state_id")));
            Task task = TaskTransformer.convertToEntity(
                    taskDto,
                    toDoService.readById(taskDto.getTodoId()),
                    stateService.readById(taskDto.getStateId())
            );
            Task oldTask = taskService.readById(Integer.valueOf(id));
            oldTask.setName(task.getName());
            oldTask.setState(task.getState());
            oldTask.setPriority(task.getPriority());
            taskService.update(oldTask);

            return getTaskMap(oldTask,HttpStatus.OK);
        }
        catch (Exception e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable String id) {

        try {
            taskService.delete(Integer.parseInt(id));
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }
    private ResponseEntity getTaskMap(Task enteredTask, HttpStatus httpStatus) {
        Map<String, String> editedMap = new LinkedHashMap<>();
        editedMap.put("id", String.valueOf(enteredTask.getId()));
        editedMap.put("name", enteredTask.getName());
        editedMap.put("priority", enteredTask.getPriority().toString());
        editedMap.put("state", enteredTask.getState().getName());
        editedMap.put("todo_id", String.valueOf(enteredTask.getTodo().getId()));
        return new ResponseEntity(editedMap, httpStatus);
    }
}
