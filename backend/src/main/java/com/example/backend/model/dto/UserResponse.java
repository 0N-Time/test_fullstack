package com.example.backend.model.dto;

import com.example.backend.model.dao.Account;

import java.math.BigDecimal;

public class UserResponse {
    public String name;
    public BigDecimal medals;
    public String equippedColor;

    public UserResponse(Account account) {
        name = account.getName();
        medals = account.getMedals();
        equippedColor = account.getEquippedColor();
    }
}
