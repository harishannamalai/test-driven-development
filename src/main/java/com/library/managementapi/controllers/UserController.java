package com.library.managementapi.controllers;

import com.library.managementapi.entities.User;
import com.library.managementapi.models.UserInfo;
import com.library.managementapi.services.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller exposes APIs for creating and viewing users.
 */

@RestController
public class UserController {

    @Autowired
    UserManagementService service;

    @GetMapping("/users")
    public List<User> listUsers(@PathVariable(name = "bookId", required = false) String userId) {
        return service.getAllUsers();
    }

    @PostMapping("/users")
    public User createUser(@RequestBody UserInfo info) {
        return service.createNewUser(info);
    }
}
