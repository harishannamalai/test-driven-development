package com.library.managementapi.models;

import lombok.Data;

@Data
public class UserInfo {
    private long userId;
    private String name;
    private String email;
    private boolean active;

}
