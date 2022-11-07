package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.StateService;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.css.Counter;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    UserRepository userRepository;
    UserService userService;
    RoleService roleService;
    ToDoService toDoService;

    @Autowired
    public UserController(UserService userService, ToDoService toDoService, RoleService roleService) {
        this.userService = userService;
        this.toDoService = toDoService;
        this.roleService = roleService;
    }


    @GetMapping("/createUser")
    public String createUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        model.addAttribute("rolesR", roleService.getAll());
        return "create-user";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user) {
        //save user to DB
        user.setRole(roleService.readById(2));
        userService.create(user);
        return "redirect:/";
    }

    @GetMapping("/read/{id}")
    public String read(@PathVariable long id, Model m){
        User user = userService.readById(id);
        m.addAttribute("user",user);
        return "user-info";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(value = "id") long id, Model model) {
        //get user from the bd
        User user = userService.readById(id);
        user.setRole(roleService.readById(2));
        model.addAttribute("rolesR", roleService.getAll());
        // set user as a model attribute to pre-populate the form
        model.addAttribute("user", user);

        return "update-user";
    }
    @PostMapping("update/{id}")
    public String updateSave(@ModelAttribute @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return  "update-user";
        }
        if (user.getPassword().isBlank()) user.setPassword(userService.readById(user.getId()).getPassword());
        userService.update(user);
        return "redirect:/home";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(value = "id") long id) {
        userService.delete(id);
        return "redirect:/";
    }
    @GetMapping("/all/{id}")
    public String getAll(@PathVariable(value = "id") long id, Model model) {
        User user = userService.readById(id);
        model.addAttribute("userFullName",user.getFirstName() + " " + user.getLastName());
        model.addAttribute("todos", toDoService.getByUserId(id));
        return "redirect:/todos/all/users/{id}";
    }
}
