package com.example.backend.model.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private GameStatus status;
    private String gameBoard;
    private TicTacToe winner;


    @OneToOne
    private Account playerOne;

    @OneToOne
    private Account playerTwo;
}