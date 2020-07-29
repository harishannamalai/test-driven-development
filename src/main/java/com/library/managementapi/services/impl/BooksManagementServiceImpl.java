package com.library.managementapi.services.impl;

import com.library.managementapi.constants.CatalogStatus;
import com.library.managementapi.entities.Book;
import com.library.managementapi.entities.CatalogEntry;
import com.library.managementapi.models.BookInfo;
import com.library.managementapi.repositories.BooksRepository;
import com.library.managementapi.repositories.CatalogRepository;
import com.library.managementapi.services.BooksManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BooksManagementServiceImpl implements BooksManagementService {
    @Autowired
    BooksRepository repository;

    @Autowired
    CatalogRepository catalogRepository;

    public Book createBook(BookInfo info) {
        Book book;
        book = repository.findByAuthorAndTitleAndRevision(info.getAuthor().trim(), info.getTitle().trim(), info.getRevision());
        if (book == null) {
            book = new Book();
            book.setAuthor(info.getAuthor().trim());
            book.setTitle(info.getTitle().trim());
            book.setRevision(info.getRevision());
            book = repository.save(book);
        }

        /* Create a Catalog Entry */
        if (book.getBookId() > 0) {
            CatalogEntry entry = new CatalogEntry();
            entry.setBookId(book.getBookId());
            entry.setStatus(CatalogStatus.AVAILABLE);
            entry = catalogRepository.save(entry);
            if (entry.getCatalogId() <= 0) {
                throw new RuntimeException("Unable to process add book to catalog!");
            }
        }
        return book;
    }

    public Book findById(long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Book> listAllBooks() {
        return repository.findAll();
    }
}
