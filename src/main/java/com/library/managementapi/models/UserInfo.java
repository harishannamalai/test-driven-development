package com.library.managementapi.models;

import lombok.Data;

@Data
public class UserInfo {
    private long userId;
    private String Name;
    private boolean active;
    private boolean createUser;
}
