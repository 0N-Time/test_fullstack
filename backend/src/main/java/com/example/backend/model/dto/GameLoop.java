package com.example.backend.model.dto;

import com.example.backend.model.dao.TicTacToe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class GameLoop {

    private TicTacToe type;
    private Integer coordinateX;
    private Integer coordinateY;
    private Long gameId;
}
