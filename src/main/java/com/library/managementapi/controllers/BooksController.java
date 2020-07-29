package com.library.managementapi.controllers;

import com.library.managementapi.entities.Book;
import com.library.managementapi.models.BookInfo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BooksController {

    @GetMapping("/books")
    public List<Book> listBooks(@PathVariable(name = "bookId", required = false) String bookId){
        return new ArrayList<>();
    }

    @PostMapping("/books")
    public Book createBook(@RequestBody BookInfo bookInfo){
        return new Book();
    }
}
