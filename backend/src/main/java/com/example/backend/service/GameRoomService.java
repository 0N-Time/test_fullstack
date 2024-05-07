package com.example.backend.service;

import com.example.backend.model.GameRoom;
import com.example.backend.Exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GameRoomService {
    Set<GameRoom> gameRooms;

    public GameRoom findGameRoomWithId(Long id) {
        for (GameRoom gameRoom : gameRooms) {
            if (gameRoom.game.getId().equals(id)) {
                return gameRoom;
            }
        }
        throw new NotFoundException("GameRoom couldn't be found");
    }

    public void addGameRoomToSet(GameRoom gameRoom) {
        gameRooms.add(gameRoom);
    }

    public void removeGameRoomFromSet(GameRoom gameRoom) {
        gameRooms.remove(gameRoom);
    }
}
