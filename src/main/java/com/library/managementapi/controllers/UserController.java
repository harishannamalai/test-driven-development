package com.library.managementapi.controllers;

import com.library.managementapi.entities.Book;
import com.library.managementapi.entities.User;
import com.library.managementapi.models.BookInfo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @GetMapping("/users")
    public List<Book> listUsers(@PathVariable(name = "bookId", required = false) String userId){
        return new ArrayList<>();
    }

    @PostMapping("/users")
    public User createUser(@RequestBody BookInfo UserInfo){
        return new User();
    }
}
