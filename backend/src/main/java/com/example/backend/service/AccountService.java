package com.example.backend.service;

import com.example.backend.controller.UpdateUserRequest;
import com.example.backend.model.dao.Account;
import com.example.backend.model.dao.BadRequestException;
import com.example.backend.model.dao.NotFoundException;
import com.example.backend.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public Integer getUserIdByUsername(String username) {
        return userRepository.findByUsername(username).map(Account::getId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Account updateUser(Integer id, UpdateUserRequest request) {
        Account user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        if (request.getUsername() != null) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new BadRequestException("Username already exists");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getName() != null) {
            if (userRepository.existsByName(request.getName())) {
                throw new BadRequestException("Name already exists");
            }
            user.setName(request.getName());
        }

        return userRepository.save(user);
    }

    public String getUsernameFromToken(String token) {
        return jwtService.extractUsername(token);
    }

    public Optional<Account> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

}
