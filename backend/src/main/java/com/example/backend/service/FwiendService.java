package com.example.backend.service;

import com.example.backend.Exception.NotFoundException;
import com.example.backend.model.dao.Account;
import com.example.backend.model.dao.Chat;
import com.example.backend.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FwiendService {

    private final JwtService jwtService;
    private final AccountService accountService;
    private final UserRepository userRepository;

    public void addFwiend(Account requester, Account receiver) {
        Chat chat = new Chat();
        chat.getAccount().add(receiver);
        requester.getChats().add(chat);
    }

    public Account getAccountFromUuid(String uuid) {
        if (userRepository.findByUuid(uuid).isPresent()) {
            return userRepository.findByUuid(uuid).get();
        } else throw new NotFoundException("Invalid UUID");
    }

    public Account getAccountFromToken(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        return accountService.findByUsername(username).orElseThrow(() -> new NotFoundException("Account doesn't exist"));
    }
}
