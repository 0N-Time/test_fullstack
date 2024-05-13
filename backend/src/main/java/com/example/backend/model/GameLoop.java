package com.example.backend.model;

import com.example.backend.model.dao.TicTacToe;
import lombok.Data;

@Data
public class GameLoop {

    private TicTacToe type;
    private Integer coordinateX;
    private Integer coordinateY;
    private Long gameId;
}
