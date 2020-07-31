package com.library.managementapi.controllers;

import com.library.managementapi.models.CatalogRequest;
import com.library.managementapi.models.CatalogResponseItem;
import com.library.managementapi.services.LibraryManagementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * This Controller exposes APIs for
 * 1. Viewing all the available books.
 * 2. Viewing Books borrowed by the particular user.
 * 3. Performing a transaction - Borrow or Return Books.
 */

@RestController
@Log4j2
public class LibraryManagementController {

    @Autowired
    LibraryManagementService service;

    @GetMapping("/catalog/books")
    public List<CatalogResponseItem> getBooksInCatalog(@PathParam("bookId") String bookId) {
        return service.getBooks(bookId);
    }

    @GetMapping("/catalog/user/{userId}")
    public List<CatalogResponseItem> getBooksOfUser(@PathVariable(name = "userId") String userId) {
        return service.getBooksByUserId(userId);
    }

    @PostMapping("/catalog/user/{userId}")
    public List<CatalogResponseItem> processUserAction(@PathVariable(name = "userId") String userId, @RequestBody CatalogRequest request) {
        return service.processUserAction(userId, request);
    }
}
