package com.library.managementapi.services;

import com.library.managementapi.models.CatalogRequest;
import com.library.managementapi.models.CatalogResponseItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LibraryManagementService {

    List<CatalogResponseItem> getBooks(String bookId);

    List<CatalogResponseItem> getBooksByUserId(String userId);

    List<CatalogResponseItem> processUserAction(String userId, CatalogRequest request);
}
