package com.library.managementapi.models;

import com.library.managementapi.constants.CatalogStatus;
import lombok.Data;

import java.sql.Date;

@Data
public class CatalogResponseItem {
    private long bookId;
    private String title;
    private String author;
    private CatalogStatus status;
    private Date borrowedDate;
    private Date returnDate;

}
