package com.example.backend.model.dto;

import com.example.backend.model.dao.Account;

public class FriendRequestResponse {
    public String name;

    public FriendRequestResponse(Account account) {
        name = account.getName();
    }
}
