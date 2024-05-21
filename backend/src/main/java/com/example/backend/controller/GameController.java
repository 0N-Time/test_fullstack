package com.example.backend.controller;

import com.example.backend.Exception.InvalidGameException;
import com.example.backend.Exception.InvalidParamException;
import com.example.backend.Exception.NotFoundException;
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
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/create-game")
    public ResponseEntity<GameResponse> start(@RequestHeader("Authorization") String authorizationHeader) {
        Account account = getAccountFromToken(authorizationHeader);
        Game game = gameService.createGame(account);
        game.setTurn(Math.random() > 0.5);
        return ResponseEntity.ok(new GameResponse(game));
    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connect(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Long gameId) throws InvalidParamException, InvalidGameException {
        Account account = getAccountFromToken(authorizationHeader);
        Game game = gameService.connectToGame(account, gameId);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestHeader("Authorization") String authorizationHeader) {
        Account account = getAccountFromToken(authorizationHeader);
        Game game = gameService.connectToRandomGame(account);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/gameLoop")
    public void gameLoop(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Move move) {
        Account account = getAccountFromToken(authorizationHeader);
        Game game = gameService.findByUser(account);
    }

    private Account getAccountFromToken(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        return accountService.findByUsername(username).orElseThrow(() -> new NotFoundException("Account doesn't exist"));
    }
}
