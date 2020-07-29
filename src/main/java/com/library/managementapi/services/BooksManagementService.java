package com.library.managementapi.services;

import com.library.managementapi.entities.Book;
import com.library.managementapi.models.BookInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BooksManagementService {

    Book createBook(BookInfo info);

    Book findById(long id);

    List<Book> listAllBooks();
}
