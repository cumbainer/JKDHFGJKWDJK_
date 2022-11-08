package com.softserve.itacademy.controller.Rest;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/todos/")
public class ToDoRestController {

    private ToDoService toDoService;
    private UserService userService;

    @Autowired
    public ToDoRestController(ToDoService toDoService, UserService userService) {
        this.toDoService = toDoService;
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<ToDo> getToDo(@PathVariable("id") Long todoId){
        if(todoId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ToDo todo = this.toDoService.readById(todoId);
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<ToDo>> getAllTodos(){
        List<ToDo> todos = toDoService.getAll();
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @GetMapping("{id}/getAll")
    public ResponseEntity<List<ToDo>> getAllTodosByUserId(@PathVariable("id") Long userId){
        User user = userService.readById(userId);
        List<ToDo> todos = user.getMyTodos();
        return new ResponseEntity<>(todos,HttpStatus.OK);
    }

    @PostMapping("{id}")
    public ResponseEntity<ToDo> createToDo(@PathVariable("id") Long userId, @RequestBody ToDo t){

        t.setOwner(userService.readById(userId));
        t.setCreatedAt(LocalDateTime.now());

        toDoService.create(t);

        return new ResponseEntity<>(t, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable("id") Long id){
        ToDo todo = toDoService.readById(id);

        if (todo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        toDoService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("")
    public ResponseEntity<ToDo> updateToDo(@RequestBody ToDo todo){
        if (todo == null) {return new ResponseEntity<>(HttpStatus.NOT_FOUND);}

        toDoService.update(todo);

        return new ResponseEntity<>(todo, HttpStatus.OK);

    }



















}
