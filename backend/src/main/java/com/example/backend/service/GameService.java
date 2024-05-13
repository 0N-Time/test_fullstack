package com.example.backend.service;

import com.example.backend.Exception.InvalidGameException;
import com.example.backend.Exception.InvalidParamException;
import com.example.backend.Exception.NotFoundException;
import com.example.backend.model.GameLoop;
import com.example.backend.model.dao.Account;
import com.example.backend.model.dao.Game;
import com.example.backend.model.dao.GameStatus;
import com.example.backend.model.repository.GameRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public Game createGame(Account account) {
        Game game = new Game();
        game.setGameBoard("000000000");
        game.setPlayerOne(account);
        game.setStatus(GameStatus.NEW);
        gameRepository.save(game);
        return game;
    }

    public Game connectToGame(Account account, Long gameId) throws InvalidParamException, InvalidGameException {
        if (!gameRepository.findById(gameId).isPresent()) {
            throw new InvalidParamException("Game with provided id doesn't exist");
        }
        Game game = gameRepository.findById(gameId).get();

        if (game.getPlayerOne() != null) {
            throw new InvalidGameException("Game is not valid anymore");
        }

        game.setPlayerOne(account);
        game.setStatus(GameStatus.IN_PROGRESS);
        gameRepository.save(game);
        return game;
    }

    public Game connectToRandomGame(Account account) {
        Game game = gameRepository
                .findAll()
                .stream()
                .filter(it -> it.getStatus().equals(GameStatus.NEW))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Game not found"));
        game.setPlayerOne(account);
        game.setStatus(GameStatus.IN_PROGRESS);
        gameRepository.save(game);
        return game;
    }

    public Game gameLoop(GameLoop gameLoop) {
        if (!gameRepository. )
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
                        gameBoard[i][j] = null;
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

    public void setGameBoard(Long id, Integer[][] gameBoardArray) {
        Optional<Game> game = gameRepository.findById(id);
        if (game.isPresent()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < gameBoardArray.length; i++) {
                for (int j = 0; j < gameBoardArray[i].length; j++) {
                    if (gameBoardArray[i][j] == null) {
                        gameBoardArray[i][j] = 0;
                    }
                    builder.append(gameBoardArray[i][j]);
                }
            }
            game.get().setGameBoard(builder.toString());
            gameRepository.save(game.get());
        }
    }
}