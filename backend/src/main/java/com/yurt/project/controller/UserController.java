package com.yurt.project.controller;

import com.yurt.project.entity.User;
import com.yurt.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/add-role")
    public ResponseEntity<String> addRoleToUser(@RequestParam String username,
                                                @RequestParam String roleName) {
        userService.addRoleToUser(username, roleName);
        return ResponseEntity.ok("Rol başarıyla atandı.");
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }
}