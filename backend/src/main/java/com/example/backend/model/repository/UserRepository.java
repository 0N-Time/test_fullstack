package com.example.backend.model.repository;

import com.example.backend.model.dao.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByName(String name);
}
