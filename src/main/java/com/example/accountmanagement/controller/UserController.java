package com.example.accountmanagement.controller;

import com.example.accountmanagement.dto.AuthenticationRequest;
import com.example.accountmanagement.dto.AuthenticationResponse;
import com.example.accountmanagement.model.User;
import com.example.accountmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user){
        return userService.register(user);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return userService.login(request);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication) {
        return userService.logout(authentication.getName());
    }
}
