package com.library.managementapi.controllers;

import com.library.managementapi.models.CatalogRequest;
import com.library.managementapi.models.CatalogResponseItem;
import com.library.managementapi.services.LibraryManagementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
public class LibraryManagementController {

    @Autowired
    LibraryManagementService service;

    @GetMapping("/catalog/books")
    public List<CatalogResponseItem> getBooksInCatalog(@PathParam("bookId") String bookId) {
        return service.getBooks(bookId);
    }

    @GetMapping("/catalog/user/{:userId}")
    public List<CatalogResponseItem> getBooksOfUser(@PathVariable(name = "userId") String userId) {
        return new ArrayList<>();
    }

    @PostMapping("/catalog/user/{:userId}")
    public List<CatalogResponseItem> processUserAction(@RequestBody CatalogRequest request){
        return new ArrayList<>();
    }
}
