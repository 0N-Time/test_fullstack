package com.example.backend.service;

import com.example.backend.Exception.InvalidGameException;
import com.example.backend.Exception.InvalidParamException;
import com.example.backend.Exception.NotFoundException;
import com.example.backend.model.dto.GameLoop;
import com.example.backend.model.dao.Account;
import com.example.backend.model.dao.Game;
import com.example.backend.model.dao.GameStatus;
import com.example.backend.model.dao.TicTacToe;
import com.example.backend.model.dto.GameResponse;
import com.example.backend.model.repository.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    public Game createGame(Account account) {
        Game game = new Game();
        game.setGameBoard("000000000");
        game.setPlayerOne(account);
        game.setStatus(GameStatus.NEW);
        if (Math.random() < 0.5) {
            game.setCurrentPlayerTurn(TicTacToe.X);
        } else {
            game.setCurrentPlayerTurn(TicTacToe.O);
        }
        gameRepository.save(game);
        return game;
    }

    public Game connectToGame(Account account, Long gameId) throws InvalidParamException, InvalidGameException {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new InvalidParamException("Game with provided id doesn't exist"));
        if (game.getPlayerOne() == null) {
            throw new InvalidGameException("PlayerOne is not present");
        }
        if (game.getPlayerTwo() != null) {
            throw new InvalidGameException("PlayerTwo already exists");
        }
        if (game.getStatus().equals(GameStatus.IN_PROGRESS)) {
            throw new InvalidGameException("Game is already in progress");
        }
        game.setPlayerTwo(account);
        game.setStatus(GameStatus.IN_PROGRESS);
        gameRepository.save(game);
        messagingTemplate.convertAndSend("/topic/game/" + game.getId(), new GameResponse(game));
        return game;
    }

    public Game connectToRandomGame(Account account) {
        Game game = gameRepository
                .findAll()
                .stream()
                .filter(it -> it.getStatus().equals(GameStatus.NEW))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No new games found"));
        game.setPlayerTwo(account);
        game.setStatus(GameStatus.IN_PROGRESS);
        gameRepository.save(game);
        messagingTemplate.convertAndSend("/topic/game/" + game.getId(), new GameResponse(game));
        return game;
    }

    public void gameLoop(GameLoop gameLoop) throws InvalidGameException {
        Game game = gameRepository.findById(gameLoop.getGameId()).orElseThrow(() -> new NotFoundException("Game not found"));

        if (game.getCurrentPlayerTurn() != gameLoop.getType()) {
            throw new InvalidGameException("Not your turn");
        }

        if (!game.getStatus().equals(GameStatus.IN_PROGRESS)) {
            throw new InvalidGameException("Game is not in progress");
        }

        Integer[][] gameBoard = getGameBoard(gameLoop.getGameId());
        if (gameBoard[gameLoop.getCoordinateX()][gameLoop.getCoordinateY()] == 1 || gameBoard[gameLoop.getCoordinateX()][gameLoop.getCoordinateY()] == 2) {
            throw new InvalidGameException("Invalid Input");
        }
        gameBoard[gameLoop.getCoordinateX()][gameLoop.getCoordinateY()] = gameLoop.getType().getValue();

        Boolean xWinner = checkWinner(gameBoard, TicTacToe.X);
        Boolean oWinner = checkWinner(gameBoard, TicTacToe.O);

        if (xWinner) {
            game.setWinner(TicTacToe.X);
            game.getPlayerOne().incrementMedals(10);
            game.getPlayerTwo().decrementMedals(1);
            game.setStatus(GameStatus.FINISHED);
        } else if (oWinner) {
            game.setWinner(TicTacToe.O);
            game.getPlayerTwo().incrementMedals(10);
            game.getPlayerOne().decrementMedals(1);
            game.setStatus(GameStatus.FINISHED);
        } else if (checkTie(gameBoard)) {
            game.setWinner(TicTacToe.TIE);
            game.getPlayerOne().incrementMedals(5);
            game.getPlayerTwo().incrementMedals(5);
            game.setStatus(GameStatus.FINISHED);
        }

        if (game.getCurrentPlayerTurn() == TicTacToe.X) {
            game.setCurrentPlayerTurn(TicTacToe.O);
        } else {
            game.setCurrentPlayerTurn(TicTacToe.X);
        }

        game.setGameBoard(transGameBoardToString(gameBoard));
        gameRepository.save(game);
        messagingTemplate.convertAndSend("/topic/game/" + game.getId(), new GameResponse(game));
        if (game.getStatus() == GameStatus.FINISHED) {
            gameRepository.delete(game);
        }
    }

    private Boolean checkWinner(Integer[][] gameBoard, TicTacToe ticTacToe) {
        int[] boardArray = new int[9];
        int counterIndex = 0;
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard.length; j++) {
                boardArray[counterIndex] = gameBoard[i][j];
                counterIndex++;
            }
        }

        int[][] winCombinations = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
        for (int i = 0; i < winCombinations.length; i++) {
            int counter = 0;
            for (int j = 0; j < winCombinations[i].length; j++) {
                if (boardArray[winCombinations[i][j]] == ticTacToe.getValue()) {
                    counter++;
                    if (counter == 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Boolean checkTie(Integer[][] gameBoard) {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard.length; j++) {
                if (gameBoard[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void leaveGame(Account account) {
        Game game = findByUser(account);
        if (game.getStatus() == GameStatus.IN_PROGRESS) {
            if (game.getPlayerOne() == account) {
                game.setWinner(TicTacToe.O);
                game.getPlayerTwo().incrementMedals(10);
                game.getPlayerOne().decrementMedals(1);
                game.setStatus(GameStatus.FINISHED);
            } else {
                game.setWinner(TicTacToe.X);
                game.getPlayerOne().incrementMedals(10);
                game.getPlayerTwo().decrementMedals(1);
                game.setStatus(GameStatus.FINISHED);
            }
            gameRepository.save(game);
            messagingTemplate.convertAndSend("/topic/game/" + game.getId(), new GameResponse(game));
            gameRepository.delete(game);
        }
        if (game.getStatus() == GameStatus.NEW) {
            game.setStatus(GameStatus.FINISHED);
            messagingTemplate.convertAndSend("/topic/game/" + game.getId(), new GameResponse(game));
            gameRepository.delete(game);
        }
    }

    public Game findByUser(Account account) {
         return gameRepository.findAll().stream()
                 .filter(game -> game.getPlayerOne().equals(account) || game.getPlayerTwo().equals(account))
                 .toList()
                 .getFirst();
    }

    public Boolean IsUserInGame(Account account) {
        Optional<Game> game = gameRepository.findByPlayerOneOrPlayerTwo(account, account);

        if (game.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public Game findById(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow();
    }


    public Integer[][] getGameBoard(Long id) {
        Optional<Game> game = gameRepository.findById(id);
        Integer[][] gameBoard = null;
        if (game.isPresent()) {
            gameBoard = new Integer[3][3];
            String gameBoardString = game.get().getGameBoard();
            int index = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (gameBoardString.charAt(index) == '0') {
                        gameBoard[i][j] = 0;
                    } else {
                        char charAtIndex = gameBoardString.charAt(index);
                        Integer intValue = Integer.parseInt(String.valueOf(charAtIndex));

                        gameBoard[i][j] = intValue;
                    }
                    index++;
                }
            }
        }
        return gameBoard;
    }

    public String transGameBoardToString(Integer[][] gameBoardArray) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < gameBoardArray.length; i++) {
                for (int j = 0; j < gameBoardArray[i].length; j++) {
                    if (gameBoardArray[i][j] == null) {
                        gameBoardArray[i][j] = 0;
                    }
                    builder.append(gameBoardArray[i][j]);
                }
            }
            return builder.toString();
    }
}