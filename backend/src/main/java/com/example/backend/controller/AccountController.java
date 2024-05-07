package com.example.backend.controller;


import com.example.backend.model.dao.Account;
import com.example.backend.model.dto.Token;
import com.example.backend.service.JwtService;
import com.example.backend.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final JwtService jwtService;

    @GetMapping("/get-name")
    public ResponseEntity<?> getUserName(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        return accountService.getAccountName(username);
    }

    @GetMapping("/get-medals")
    public ResponseEntity<?> getMedals(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        return accountService.getAccountMedals(username);
    }

    @PutMapping("/update")
    public ResponseEntity<Token> updateUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UpdateUserRequest request) {
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        Integer userId = accountService.getUserIdByUsername(username);
        Account user = accountService.updateUser(userId, request);

        String jwt = jwtService.generateToken(user);

        return ResponseEntity.ok(new Token(jwt));
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestHeader(value = "Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);
        Account user = accountService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        accountService.deleteUser(user.getId());

        return ResponseEntity.ok().build();
    }

}
