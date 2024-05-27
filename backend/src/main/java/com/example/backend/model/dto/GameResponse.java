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
    public String playerX;
    public String playerO;

    public GameResponse (Game game) {
        id = game.getId();
        status = game.getStatus();
        gameBoard = game.getGameBoard();
        winner = game.getWinner();
        currentPlayerTurn = game.getCurrentPlayerTurn();
        if (game.getPlayerOne() != null) {
            playerX = game.getPlayerOne().getName();
        }
        if (game.getPlayerTwo() != null) {
            playerO = game.getPlayerTwo().getName();
        }
    }
}