package com.example.backend.controller;

import com.example.backend.model.dao.Chat;
import com.example.backend.model.dto.GameResponse;
import com.example.backend.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final GameService gameService;

    @MessageMapping("/game/{uid}")
    @SendTo("/topic/game/{uid}")
    public GameResponse game(@DestinationVariable("uid") String uid) {
        return new GameResponse(gameService.findByUid(uid));
    }
    /*
    @MessageMapping("/chat/{id}")
    @SendTo("topic/chat/{id}")
    public Chat chat() {

    }*/
}
