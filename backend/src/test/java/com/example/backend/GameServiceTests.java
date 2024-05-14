package com.example.backend;

import com.example.backend.model.dao.Game;
import com.example.backend.model.repository.GameRepository;
import com.example.backend.service.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTests {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    void testGetGameBoard() {
        // Arrange
        String gameBoardString = "023406780";
        Game game = new Game();
        game.setGameBoard(gameBoardString);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        // Act
        Integer[][] gameBoard = gameService.getGameBoard(1L);

        // Assert
        Integer[][] expectedGameBoard = {
                {null, 2, 3},
                {4, null, 6},
                {7, 8, null}
        };
        assertArrayEquals(expectedGameBoard, gameBoard);
    }

}