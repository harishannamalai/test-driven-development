package com.library.managementapi.services;

import com.library.managementapi.entities.User;
import com.library.managementapi.models.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserManagementService {
    User createNewUser(UserInfo info);

    List<User> getAllUsers();
}
