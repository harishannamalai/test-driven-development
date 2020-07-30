package com.library.managementapi.repositories;

import com.library.managementapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByNameAndEmail(String name, String email);
}
