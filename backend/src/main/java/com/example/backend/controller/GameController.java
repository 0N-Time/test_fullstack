package com.example.backend.controller;

import com.example.backend.model.dao.Account;
import com.example.backend.Exception.NotFoundException;
import com.example.backend.model.repository.UserRepository;
import com.example.backend.service.GameService;
import com.example.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final GameService gameService;

}
