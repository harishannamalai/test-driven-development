package com.library.managementapi.models;

import com.library.managementapi.constants.CatalogUserAction;
import lombok.Data;

@Data
public class CatalogRequest {
    private long bookId;
    private CatalogUserAction action;
}
