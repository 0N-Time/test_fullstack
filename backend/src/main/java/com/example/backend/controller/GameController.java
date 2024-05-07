package com.example.backend.controller;

import com.example.backend.model.dao.Account;
import com.example.backend.Exception.NotFoundException;
import com.example.backend.model.repository.UserRepository;
import com.example.backend.service.GameService;
import com.example.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final GameService gameService;

    @PostMapping
    public ResponseEntity<?> createMatch(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        Optional<Account> accountOptional = userRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            return ResponseEntity.ok(gameService.createGame(accountOptional.get()));
        } else {
            throw new NotFoundException("Account can't be found");
        }
    }
/*
    @PostMapping("/join/{gameId}")
    public ResponseEntity<?> joinMatch(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long gameId) {
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        Optional<Account> accountOptional = userRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            return ResponseEntity.ok(gameService.joinGame(accountOptional.get(), gameId));
        } else {
            throw new NotFoundException("Account can't be found");
        }
    }
*/
}
