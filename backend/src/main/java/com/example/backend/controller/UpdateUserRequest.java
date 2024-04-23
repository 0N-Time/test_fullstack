package com.example.backend.controller;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private Integer id;
    private String username;
    private String password;
    private String name;
}
