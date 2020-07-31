package com.library.managementapi.services.impl;

import com.library.managementapi.constants.CatalogStatus;
import com.library.managementapi.entities.Book;
import com.library.managementapi.entities.CatalogEntry;
import com.library.managementapi.models.CatalogRequest;
import com.library.managementapi.models.CatalogResponseItem;
import com.library.managementapi.repositories.BooksRepository;
import com.library.managementapi.repositories.CatalogRepository;
import com.library.managementapi.services.LibraryManagementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.library.managementapi.constants.ApplicationConstants.BOOK_LIMIT_PER_USER;
import static com.library.managementapi.constants.ApplicationConstants.DEFAULT_BORROW_PERIOD;

@Component
@Log4j2
public class LibraryManagementServiceImpl implements LibraryManagementService {

    @Autowired
    CatalogRepository repository;

    @Autowired
    BooksRepository booksRepository;

    @Override
    public List<CatalogResponseItem> getBooks(String bookId) {
        if (bookId == null) {
            return this.getAllAvailableBooks();
        } else {
            try {
                return this.getAllBooksByBookId(Long.parseLong(bookId));
            } catch (NumberFormatException e) {
                log.warn("Book Id cannot be parsed as Long!" + bookId);
                return new ArrayList<>();
            }
        }
    }


    @Override
    public List<CatalogResponseItem> getBooksByUserId(String userId) {
        if (userId != null) {
            try {
                return this.getAllBooksByUserId(Long.parseLong(userId));
            } catch (NumberFormatException e) {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<CatalogResponseItem> processUserAction(String userId, CatalogRequest request) {
        assert request != null;
        long id = 0;
        try {
            id = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot parse userId as Long: " + userId);
        }
        switch (request.getAction()) {
            case BORROW:
                return this.processBorrowAction(id, request.getBookId());
            case RETURN:
                return this.processReturnAction(id, request.getBookId());
            default:
                throw new IllegalArgumentException("Requested Action Not Implemented!");
        }
    }

    private List<CatalogResponseItem> processBorrowAction(long userId, long bookId) {
        List<CatalogEntry> entries = repository.findAllByUserId(userId);
        boolean borrowedAlready = entries.stream().map(CatalogEntry::getBookId).anyMatch(e -> e == bookId);
        if (entries.size() <= BOOK_LIMIT_PER_USER && !borrowedAlready) {
            CatalogEntry bookEntry = repository.findAllByBookIdAndStatusOrderByBookIdAsc(bookId, CatalogStatus.AVAILABLE).stream().findFirst().orElse(null);
            if (bookEntry == null) {
                throw new IllegalArgumentException("Book Id not available or Invalid!");
            } else {
                bookEntry.setStatus(CatalogStatus.BORROWED);
                bookEntry.setUserId(userId);
                bookEntry.setBorrowedDate(new Date(System.currentTimeMillis()));
                bookEntry.setReturnDate(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(DEFAULT_BORROW_PERIOD)));
                repository.save(bookEntry);
                entries.add(bookEntry);
            }
        }
        return this.getCatalogResponseItemDetails(entries);
    }

    private List<CatalogResponseItem> processReturnAction(long userId, long bookId) {
        List<CatalogEntry> entries = repository.findAllByUserId(userId);
        boolean borrowedAlready = entries.stream().map(CatalogEntry::getBookId).anyMatch(e -> e == bookId);
        if (borrowedAlready) {
            CatalogEntry bookEntry = repository.findAllByUserIdAndBookIdAndStatusOrderByBookIdAsc(userId, bookId, CatalogStatus.BORROWED).stream().findFirst().orElse(null);
            if (bookEntry == null) {
                throw new IllegalArgumentException("Book Not Borrowed by user or Invalid!");
            } else {
                bookEntry.setStatus(CatalogStatus.AVAILABLE);
                bookEntry.setUserId(0);
                bookEntry.setBorrowedDate(null);
                bookEntry.setReturnDate(null);
                repository.save(bookEntry);
                entries = entries.stream().filter(e -> e.getBookId() != bookId).collect(Collectors.toList());
            }
        }
        return this.getCatalogResponseItemDetails(entries);
    }

    private List<CatalogResponseItem> getAllAvailableBooks() {
        return this.getCatalogResponseItemDetails(repository.findAllByStatusOrderByBookIdAsc(CatalogStatus.AVAILABLE));
    }

    private List<CatalogResponseItem> getAllBooksByBookId(long bookId) {
        return this.getCatalogResponseItemDetails(repository.findAllByBookIdAndStatusOrderByBookIdAsc(bookId, CatalogStatus.AVAILABLE));
    }

    private List<CatalogResponseItem> getAllBooksByUserId(long userId) {
        return this.getCatalogResponseItemDetails(repository.findAllByUserId(userId));
    }

    private List<CatalogResponseItem> getCatalogResponseItemDetails(List<CatalogEntry> entries) {
        return entries
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
