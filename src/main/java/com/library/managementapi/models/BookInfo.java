package com.library.managementapi.models;

import lombok.Data;

@Data
public class BookInfo {

    private String bookId;
    private String title;
    private String author;
    private int revision;

}
