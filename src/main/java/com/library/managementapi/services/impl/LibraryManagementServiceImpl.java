package com.library.managementapi.services.impl;

import com.library.managementapi.constants.CatalogStatus;
import com.library.managementapi.entities.Book;
import com.library.managementapi.models.CatalogResponseItem;
import com.library.managementapi.repositories.BooksRepository;
import com.library.managementapi.repositories.CatalogRepository;
import com.library.managementapi.services.LibraryManagementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log4j2
public class LibraryManagementServiceImpl implements LibraryManagementService {

    @Autowired
    CatalogRepository repository;

    @Autowired
    BooksRepository booksRepository;

    public List<CatalogResponseItem> getBooks(String bookId) {
        if (bookId == null) {
            return this.getAllAvailableBooks();
        } else {
            return new ArrayList<>();
        }
    }

    private List<CatalogResponseItem> getAllAvailableBooks() {
        return repository
                .findAllByStatus(CatalogStatus.AVAILABLE)
                .stream()
                .map(e -> {
                    log.info(e);
                    CatalogResponseItem responseItem = new CatalogResponseItem();
                    BeanUtils.copyProperties(e, responseItem);
                    Book book = booksRepository.findById(e.getBookId()).orElseThrow(() -> new IllegalArgumentException("Catalog Entry present without Book Entry!"));
                    BeanUtils.copyProperties(book, responseItem);
                    log.info(responseItem);
                    return responseItem;
                }).collect(Collectors.toList());
    }

}
