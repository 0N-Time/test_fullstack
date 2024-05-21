package com.example.backend.controller;

import com.example.backend.Exception.NotFoundException;
import com.example.backend.model.dao.Account;
import com.example.backend.model.dao.Game;
import com.example.backend.model.dto.GameResponse;
import com.example.backend.model.repository.GameRepository;
import com.example.backend.service.AccountService;
import com.example.backend.service.GameService;
import com.example.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final GameService gameService;
    private final JwtService jwtService;
    private final AccountService accountService;

    @MessageMapping("/game/{id}")
    @SendTo("/topic/game/{id}")
    public GameResponse game(@DestinationVariable("id") Long id) {
        return new GameResponse(gameService.findById(id));
    }



    private Account getAccountFromToken(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        return accountService.findByUsername(username).orElseThrow(() -> new NotFoundException("Account doesn't exist"));
    }
}
