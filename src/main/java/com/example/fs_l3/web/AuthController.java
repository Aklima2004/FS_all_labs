package com.example.fs_l3.web;

import com.example.fs_l3.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {
    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwt;

    public AuthController(AuthenticationManager authManager,
                          UserDetailsService userDetailsService,
                          JwtService jwt) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwt = jwt;
    }

    public record AccountCredentials(String username, String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AccountCredentials creds) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(creds.username(), creds.password()));
        var user = userDetailsService.loadUserByUsername(creds.username());
        var token = jwt.generateToken(user);
        return ResponseEntity.ok(Map.of(
                "username", user.getUsername(),
                "roles", user.getAuthorities(),
                "token", token
        ));
    }
}
