package com.example.backend.model.dto;

import com.example.backend.model.dao.Game;
import com.example.backend.model.dao.GameStatus;
import com.example.backend.model.dao.TicTacToe;

public class GameResponse {
    public Long id;
    public GameStatus status;
    public String gameBoard;
    public TicTacToe winner;
    public TicTacToe currentPlayerTurn;

    public GameResponse (Game game) {
        id = game.getId();
        status = game.getStatus();
        gameBoard = game.getGameBoard();
        winner = game.getWinner();
        currentPlayerTurn = game.getCurrentPlayerTurn();
    }
}