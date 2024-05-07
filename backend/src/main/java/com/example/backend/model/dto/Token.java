package com.example.backend.model.dto;

import lombok.Data;

@Data
public class Token {

    private String token;

    public Token(String jwt) {
        this.token = jwt;
    }
}
