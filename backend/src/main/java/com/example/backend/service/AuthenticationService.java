package com.example.backend.service;

import com.example.backend.controller.AuthenticationResponse;
import com.example.backend.controller.RegisterRequest;
import com.example.backend.controller.AuthenticationRequest;
import com.example.backend.model.dao.Account;
import com.example.backend.model.dao.Color;
import com.example.backend.model.dao.Role;
import com.example.backend.model.repository.ColorRepository;
import com.example.backend.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ColorRepository colorRepository;

    public AuthenticationResponse register(RegisterRequest request) {

        if (repository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (repository.existsByName(request.getName())) {
            throw new RuntimeException("Name already exists");
        }

        Color defaultColor = colorRepository.findColorByName("White").orElseThrow();

        var account = Account.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(Role.USER)
                .medals(BigDecimal.valueOf(0))
                .equippedColor("#FFFFFF")
                .colors(Set.of(defaultColor))
                .build();
        repository.save(account);
        var jwtToken = jwtService.generateToken(account);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var account = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(account);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
