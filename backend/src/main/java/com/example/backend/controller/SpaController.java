package com.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
public class SpaController {

    @GetMapping("/")
    public RedirectView redirectFromHomepage() {
        return new RedirectView("/index.html");
    }

    @GetMapping("/login")
    public RedirectView redirectFromLogin() {
        return new RedirectView("/index.html");
    }

    @GetMapping("/register")
    public RedirectView redirectFromRegister() {
        return new RedirectView("/index.html");
    }

    @GetMapping("/user-home/settings")
    public RedirectView redirectFromSettings() {
        return new RedirectView("/index.html");
    }

    @GetMapping("/user-home/shop")
    public RedirectView redirectFromShop() {
        return new RedirectView("/index.html");
    }

    @GetMapping("/user-home/tic-tac-toe")
    public RedirectView redirectFromTictactoe() {
        return new RedirectView("/index.html");
    }

    @GetMapping("/user-home")
    public RedirectView redirectFromUserhome() {
        return new RedirectView("/index.html");
    }

    //Error code's

    @GetMapping("/401")
    public RedirectView redirectFrom401() {
        return new RedirectView("/index.html");
    }

    @GetMapping("/403")
    public RedirectView redirectFrom403() {
        return new RedirectView("/index.html");
    }

    @GetMapping("/404")
    public RedirectView redirectFrom404() {
        return new RedirectView("/index.html");
    }
}
