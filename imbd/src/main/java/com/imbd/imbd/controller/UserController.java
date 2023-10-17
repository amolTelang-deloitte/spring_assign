package com.imbd.imbd.controller;

import com.imbd.imbd.entity.User;
import com.imbd.imbd.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/imdb")
public class UserController {
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService){

        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUserById(@RequestParam(required = true)Long userId){
        return userService.getUserDetails(userId);
    }



}
