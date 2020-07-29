package com.library.managementapi.controllers;

import com.library.managementapi.entities.Book;
import com.library.managementapi.models.BookInfo;
import com.library.managementapi.services.BooksManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BooksController {

    @Autowired
    BooksManagementService service;

    @GetMapping("/books")
    public List<Book> listBooks(@PathVariable(name = "bookId", required = false) String bookId) {
        return service.listAllBooks();
    }

    @PostMapping("/books")
    public Book createBook(@RequestBody BookInfo bookInfo) {
        return service.createBook(bookInfo);
    }
}
