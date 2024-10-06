package com.example.accountmanagement.service;

import com.example.accountmanagement.config.JwtService;
import com.example.accountmanagement.config.TokenStore;
import com.example.accountmanagement.dto.AuthenticationRequest;
import com.example.accountmanagement.dto.AuthenticationResponse;
import com.example.accountmanagement.exception.ApiException;
import com.example.accountmanagement.model.User;
import com.example.accountmanagement.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenStore tokenStore;

    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity<String> register(User user){
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(userRepository.create(user) == 0){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("User created successfully");
    }

    public ResponseEntity<AuthenticationResponse> login(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
            String activeToken = tokenStore.getActiveToken(user.getUsername());
            try {
                if (activeToken != null && !jwtService.isTokenExpired(activeToken)) {
                    throw new ApiException("You have an active token please use it or logout then login",HttpStatus.UNAUTHORIZED.value());
                }
            } catch (ExpiredJwtException e){
                tokenStore.removeToken(user.getUsername());
            }
            String jwtToken = jwtService.generateToken(user);
            tokenStore.storeToken(user.getUsername(), jwtToken);
            AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwtToken , user.getUsername());

            return ResponseEntity.ok(authenticationResponse);

        } catch (BadCredentialsException e) {
            throw new ApiException("Username or password is not valid",HttpStatus.UNAUTHORIZED.value());
        }
    }

    public ResponseEntity<String> logout(String username){
        tokenStore.removeToken(username);
        return ResponseEntity.ok().body("logged out successfully");
    }
}
