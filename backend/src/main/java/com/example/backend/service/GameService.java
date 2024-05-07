package com.example.backend.service;

import com.example.backend.model.GameRoom;
import com.example.backend.model.dao.Account;
import com.example.backend.model.dao.Game;
import com.example.backend.Exception.NotFoundException;
import com.example.backend.model.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final GameRoomService gameRoomService;

    public Game createGame(Account playerOne) {
        Game game = Game.builder()
                .playerOne(playerOne)
                .isOver(false)
                .waitingForPlayer(true)
                .gameBoard("000000000")
                .build();
        GameRoom gameRoom = GameRoom.builder()
                .game(game)
                .build();
        gameRoomService.addGameRoomToSet(gameRoom);
        return gameRepository.save(game);
    }
/*
    public Game joinGame(Account playerTwo, Long gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        if (game.isPresent()) {
            game.get().setPlayerTwo(playerTwo);
            game.get().setWaitingForPlayer(false);
            game.get().setCurrentPlayer(
                    ((int) (Math.random() * 2) + 1)
            );
            return game.get();
        } else {
            throw new NotFoundException("Game cannot be found");
        }
    }
*/
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