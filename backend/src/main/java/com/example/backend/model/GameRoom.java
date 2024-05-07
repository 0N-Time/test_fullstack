package com.example.backend.model;

import com.example.backend.model.dao.Game;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
@Builder
public class GameRoom {
    public Game game;
    public WebSocketSession sessionOne;
    public WebSocketSession sessionTwo;
}
