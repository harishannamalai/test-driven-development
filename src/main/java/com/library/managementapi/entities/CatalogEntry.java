package com.library.managementapi.entities;

import com.library.managementapi.constants.CatalogStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

/**
 * This Class Represents the Catalog of the library, along with the book self and borrowed Data.
 * <p>
 * Each physical book represents one entry, which can be either Available or Borrowed etc.
 * <p>
 * User Id represents the book that is borrowed by each user.
 */

@Data
@Entity
public class CatalogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long catalogId;
    private long bookId;
    private long userId;
    private int revision;
    private CatalogStatus status;
    private Date borrowedDate;
    private Date returnDate;

}
