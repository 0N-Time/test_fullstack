package com.example.backend.controller;

import com.example.backend.Exception.InvalidGameException;
import com.example.backend.Exception.InvalidParamException;
import com.example.backend.Exception.NotFoundException;
import com.example.backend.model.dao.TicTacToe;
import com.example.backend.model.dto.GameLoop;
import com.example.backend.model.dao.Account;
import com.example.backend.model.dao.Game;
import com.example.backend.model.dto.GameResponse;
import com.example.backend.model.dto.Move;
import com.example.backend.service.AccountService;
import com.example.backend.service.GameService;
import com.example.backend.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;
    private final JwtService jwtService;
    private final AccountService accountService;

    @PostMapping("/create-game")
    public ResponseEntity<GameResponse> start(@RequestHeader("Authorization") String authorizationHeader) {
        Account account = getAccountFromToken(authorizationHeader);
        Game game = gameService.createGame(account);
        return ResponseEntity.ok(new GameResponse(game));
    }

    @PostMapping("/connect/{id}")
    public ResponseEntity<GameResponse> connect(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) throws InvalidParamException, InvalidGameException {
        Account account = getAccountFromToken(authorizationHeader);
        Game game = gameService.connectToGame(account, id);
        return ResponseEntity.ok(new GameResponse(game));
    }

    @PostMapping("/connect/random")
    public ResponseEntity<GameResponse> connectRandom(@RequestHeader("Authorization") String authorizationHeader) {
        Account account = getAccountFromToken(authorizationHeader);
        Game game = gameService.connectToRandomGame(account);
        return ResponseEntity.ok(new GameResponse(game));
    }

    @PostMapping("/gameLoop")
    public void gameLoop(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Move move) throws InvalidGameException {
        Account account = getAccountFromToken(authorizationHeader);
        Game game = gameService.findByUser(account);

        if (game.getPlayerOne() == account) {
            gameService.gameLoop(new GameLoop(TicTacToe.X, move.getCoordinateX(), move.getCoordinateY(), game.getId()));
        } else if (game.getPlayerTwo() == account) {
            gameService.gameLoop(new GameLoop(TicTacToe.O, move.getCoordinateX(), move.getCoordinateY(), game.getId()));
        }
    }

    @GetMapping("/game-reconnect")
    public ResponseEntity<GameResponse> gameReconnect(@RequestHeader("Authorization") String authorizationHeader) {
        Account account = getAccountFromToken(authorizationHeader);
        Game game = gameService.findByUser(account);

        return ResponseEntity.ok(new GameResponse(game));
    }

    @GetMapping("/game/check-for-game")
    public ResponseEntity<Boolean> checkForGame(@RequestHeader("Authorization") String authorizationHeader) {
        Account account = getAccountFromToken(authorizationHeader);
        return ResponseEntity.ok(gameService.IsUserInGame(account));
    }

    @PostMapping("/game-surrender")
    public void surrender(@RequestHeader("Authorization") String authorizationHeader) {
        Account account = getAccountFromToken(authorizationHeader);
        gameService.leaveGame(account);
    }

    private Account getAccountFromToken(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        return accountService.findByUsername(username).orElseThrow(() -> new NotFoundException("Account doesn't exist"));
    }
}
