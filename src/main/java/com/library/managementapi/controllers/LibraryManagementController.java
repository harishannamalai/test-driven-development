package com.library.managementapi.controllers;

import com.library.managementapi.models.CatalogRequest;
import com.library.managementapi.models.CatalogResponseItem;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LibraryManagementController {

    @GetMapping("/catalog/books")
    public List<CatalogResponseItem> getBooksInCatalog(@PathParam("bookId") String bookId){
        return new ArrayList<>();
    }

    @GetMapping("/catalog/user/{:userId}")
    public List<CatalogResponseItem> getBooksOfUser(@PathVariable(name = "userId") String userId){
        return new ArrayList<>();
    }

    @PostMapping("/catalog/user/{:userId}")
    public List<CatalogResponseItem> processUserAction(@RequestBody CatalogRequest request){
        return new ArrayList<>();
    }
}
