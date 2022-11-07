package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/todos")
public class ToDoController {

    @Autowired
    ToDoService toDoService;
    @Autowired
    UserService userService;


    @GetMapping("/create/users/{owner_id}")
    public String create(Model model, @PathVariable("owner_id") long ownerId) {
        User user = userService.readById(ownerId);
        String userName = user.getFirstName() + " " + user.getLastName();
        model.addAttribute("todo", new ToDo());
        model.addAttribute("ownerName", userName);
        return "create-todo";
    }

    @PostMapping("/create/users/{owner_id}")
    public String create(
            @PathVariable("owner_id") @Valid long ownerId,
            @ModelAttribute("todo") @Valid ToDo toDo
    ) {
        toDo.setOwner(userService.readById(ownerId));
        toDo.setCreatedAt(LocalDateTime.now());
        toDoService.create(toDo);
        return "redirect:/todos/all/users/{owner_id}";
    }
    //
    @GetMapping("/{id}/tasks")
    public String read(@PathVariable(name="id") long id,
                       Model model) {
        ToDo todo = toDoService.readById(id);
        User user = todo.getOwner();
        List<User> users = userService.getAll();
        List<User> collaborators = todo.getCollaborators();
        model.addAttribute("tasks", todo.getTasks());
        model.addAttribute("todo", todo);
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        model.addAttribute("collaborators", collaborators);
        model.addAttribute("users", users);
        model.addAttribute("userName", user.getFirstName());
        model.addAttribute("todoTitle", todo.getTitle());

        return "todo-tasks";
    }

    @GetMapping("/{todo_id}/update/users/{owner_id}")
    public String update(Model model,
                         @PathVariable("todo_id") long todo_id,
                         @PathVariable("owner_id") @Valid long ownerId
    ) {
        ToDo todo = toDoService.readById(todo_id);
        User user = userService.readById(ownerId);
        String userName = user.getFirstName() + " " + user.getLastName();
        model.addAttribute("todo", todo);
        model.addAttribute("ownerName", userName);
        return "update-todo";
    }
    //
    @PostMapping("/{todo_id}/update/users/{owner_id}")
    public String update(
            @ModelAttribute("todo") @Valid ToDo toDo,
            @PathVariable("todo_id") long todo_id
    ) {
        ToDo existingTodo = toDoService.readById(todo_id);
        existingTodo.setTitle(toDo.getTitle());
        toDoService.update(existingTodo);

        return "redirect:/todos/all/users/{owner_id}";
    }
    //
    @GetMapping("/{todo_id}/delete/users/{owner_id}")
    public String delete(
            @PathVariable("todo_id") long todoId,
            @PathVariable("owner_id") long ownerId
    ) {
        List<ToDo> todos = toDoService.getByUserId(ownerId);
        ToDo toDoRes = todos.stream()
                .filter(toDo1 -> toDo1.getId() == todoId)
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
        toDoService.delete(toDoRes.getId());
        return "redirect:/todos/all/users/{owner_id}";
    }
    //
    @GetMapping("/all/users/{user_id}")
    public String getAll(
            @PathVariable("user_id") long userId,
            Model model
    ) {
        List<ToDo> todos = toDoService.getByUserId(userId);
        model.addAttribute("todos", todos);
        model.addAttribute("ownerFirstName", userService.readById(userId).getFirstName());
        model.addAttribute("ownerLastName", userService.readById(userId).getLastName());
        return "todos-user";
    }

    @GetMapping("/{id}/add")
    public String addCollaborator(@PathVariable(name="id") long todoId,
                                  @ModelAttribute(name="collaborator") User user)
                                   {


        ToDo toDo = toDoService.readById(todoId);
        List<User> collaborators = toDo.getCollaborators();
        collaborators.add(userService.readById(user.getId()));
        toDo.setCollaborators(collaborators);
        toDoService.update(toDo);
        return "redirect:/todos/{todo_id}/tasks";
    }
    //
    @GetMapping("{todo_id}/{id}/remove")
    public String removeCollaborator(
            @PathVariable("todo_id") long todo_id,
            @PathVariable("id") long id
    ) {
        ToDo todo = toDoService.readById(todo_id);

        List<User> collaborators = todo.getCollaborators();
        List<User> newCollaborators = collaborators.stream()
                .filter(user1 -> !(user1.getId() == id))
                .collect(Collectors.toList());

        todo.setCollaborators(newCollaborators);
        toDoService.update(todo);
        return "redirect:/todos/{todo_id}/tasks";

    }
}

