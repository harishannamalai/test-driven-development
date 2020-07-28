package com.library.managementapi.models;

import lombok.Data;

@Data
public class BookInfo {
    private long bookId;
    private String title;
    private String author;
    private boolean createCatalogEntry;
}
