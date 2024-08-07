package com.example.backend.model.repository;

import com.example.backend.model.dao.Account;
import com.example.backend.model.dao.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    Optional<Game> findByUid(String uid);
    Optional<Game> findByPlayerOneOrPlayerTwo(Account account1, Account account2);
}