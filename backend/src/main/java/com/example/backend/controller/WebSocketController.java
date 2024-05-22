package com.example.backend.controller;

import com.example.backend.Exception.InvalidGameException;
import com.example.backend.Exception.NotFoundException;
import com.example.backend.model.dao.Account;
import com.example.backend.model.dao.Game;
import com.example.backend.model.dao.TicTacToe;
import com.example.backend.model.dto.GameLoop;
import com.example.backend.model.dto.GameResponse;
import com.example.backend.model.dto.Move;
import com.example.backend.model.repository.GameRepository;
import com.example.backend.service.AccountService;
import com.example.backend.service.GameService;
import com.example.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final GameService gameService;

    @MessageMapping("/game/{id}")
    @SendTo("/topic/game/{id}")
    public GameResponse game(@DestinationVariable("id") Long id) {
        return new GameResponse(gameService.findById(id));
    }
}
