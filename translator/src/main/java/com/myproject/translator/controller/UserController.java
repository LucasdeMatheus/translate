package com.myproject.translator.controller;

import com.myproject.translator.domain.user.LogDTO;
import com.myproject.translator.domain.user.UserDTO;
import com.myproject.translator.domain.user.UserRepository;
import com.myproject.translator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        return userService.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO){
        return userService.createUser(userDTO);
    }

    @PostMapping("/{id}/log")
    public ResponseEntity<?> createLog(@PathVariable("id") Long id,@RequestBody LogDTO logDTO){
        return userService.createLog(id, logDTO);
    }
}
