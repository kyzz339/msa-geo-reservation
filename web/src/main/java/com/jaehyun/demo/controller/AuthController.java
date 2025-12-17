package com.jaehyun.demo.controller;

import com.jaehyun.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/signUp")
    public String signUp() {
        return "/auth/signUp";
    }

    @GetMapping("/signIn")
    public String signIn() {
        return "/auth/signIn";
    }

}
