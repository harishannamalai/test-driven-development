package com.library.managementapi.services;

import com.library.managementapi.models.CatalogResponseItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LibraryManagementService {

    List<CatalogResponseItem> getBooks(String bookId);
}
