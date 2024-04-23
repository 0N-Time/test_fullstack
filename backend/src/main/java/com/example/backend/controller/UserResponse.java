package com.example.backend.controller;

import com.example.backend.model.dao.Account;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private String message;
    private Account account;
}
