package com.example.backend.service;

import com.example.backend.controller.UpdateUserRequest;
import com.example.backend.model.dao.Account;
import com.example.backend.Exception.NotFoundException;
import com.example.backend.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Predicate<String> stringCheck = (stringVal) -> stringVal != null && !stringVal.isBlank();

    public ResponseEntity<String> getAccountName(String username) {
        Optional<Account> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get().getName());
        }
        throw new NotFoundException("User not found");
    }

    public ResponseEntity<BigDecimal> getAccountMedals(String username) {
        Optional<Account> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get().getMedals());
        }
        throw new NotFoundException("User not found");
    }

    public Integer getUserIdByUsername(String username) {
        return userRepository.findByUsername(username).map(Account::getId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Account updateUser(Integer id, UpdateUserRequest request) {
        Account user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        if (stringCheck.test(request.getUsername())) {
            if (!userRepository.existsByUsername(request.getUsername())) {
                user.setUsername(request.getUsername());
            }
        }

        if (stringCheck.test(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (stringCheck.test(request.getName())) {
            user.setName(request.getName());
        }

        return userRepository.save(user);
    }

    public Optional<Account> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

}
