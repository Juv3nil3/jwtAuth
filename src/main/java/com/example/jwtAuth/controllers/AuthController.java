package com.example.jwtAuth.controllers;

import com.example.jwtAuth.dtos.requests.JwtRequest;
import com.example.jwtAuth.dtos.responses.JwtResponse;
import com.example.jwtAuth.security.JwtHelper;
import com.example.jwtAuth.services.AuthService;
import com.example.jwtAuth.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<String> register (@Valid @RequestBody JwtRequest request){
        try{
            authService.registerUser(request.getEmail(), request.getPassword());
            return ResponseEntity.ok("User Registered Successfully");
        }
        catch (Exception e) {
            logger.error("Error occurred during user registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody JwtRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();

        String token = authService.authenticateUserAndGetToken(email, password);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(email) // Assuming email is the username
                .build();
        return ResponseEntity.ok(response);
    }
}
