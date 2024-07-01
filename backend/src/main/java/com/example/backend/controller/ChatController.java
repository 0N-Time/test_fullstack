/*package com.example.backend.controller;

import com.example.backend.Exception.NotFoundException;
import com.example.backend.model.dao.Account;
import com.example.backend.service.AccountService;
import com.example.backend.service.FwiendService;
import com.example.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final FwiendService fwiendService;

    @RequestMapping("/add")
    public ResponseEntity<> addFriend(@RequestHeader("Authorization") String authorizationHeader, @RequestBody String uuid)  {
        fwiendService.addFwiend(fwiendService.getAccountFromToken(authorizationHeader), fwiendService.getAccountFromUuid(uuid));
    }
}*/
