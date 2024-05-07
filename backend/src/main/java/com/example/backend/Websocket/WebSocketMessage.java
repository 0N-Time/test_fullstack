package com.example.backend.Websocket;

public class WebSocketMessage {
    public enum type {
        JOIN,
        PLAY
    }
    public String message;
}
