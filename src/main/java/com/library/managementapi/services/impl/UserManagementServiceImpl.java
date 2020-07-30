package com.library.managementapi.services.impl;

import com.library.managementapi.entities.User;
import com.library.managementapi.models.UserInfo;
import com.library.managementapi.repositories.UserRepository;
import com.library.managementapi.services.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    UserRepository repository;

    @Override
    public User createNewUser(UserInfo info) {
        User user = Optional
                .ofNullable(repository.findByNameAndEmail(info.getName().trim(), info.getEmail().trim()))
                .orElse(new User());

        if (user.getUserId() <= 0) {
            user.setActive(true);
            user.setName(info.getName().trim());
            user.setEmail(info.getEmail().trim());
            user = repository.save(user);
            if (user.getUserId() <= 0) {
                throw new RuntimeException("User Not created!");
            }
        }

        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }
}
