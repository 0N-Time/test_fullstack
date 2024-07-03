package com.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class SpaController {

    @GetMapping({"", "/login", "/register", "/user-home", "/user-home/settings",
            "/user-home/shop", "/user-home/tic-tac-toe", "/401", "/403", "/404"})
    public RedirectView redirectAll() {
        return new RedirectView("/index.html");
    }
}
