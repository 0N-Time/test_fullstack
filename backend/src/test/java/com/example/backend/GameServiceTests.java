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
    public void testGetGameBoard() {
        // Set up a game with a known game board string
        Game game = new Game();
        game.setId(1L);
        game.setGameBoard("012345678"); // example game board string

        // Mock the repository to return this game
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        // Set up the expected board
        Integer[][] expectedBoard = {
                {0, 1, 2},
                {3, 4, 5},
                {0, 7, 8}
        };

        // Get the actual board from the service
        Integer[][] actualBoard = gameService.getGameBoard(1L);

        // Assert that the actual board matches the expected board
        assertArrayEquals(expectedBoard, actualBoard);
    }

}