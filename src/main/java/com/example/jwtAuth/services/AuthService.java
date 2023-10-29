package com.example.jwtAuth.services;

import com.example.jwtAuth.controllers.AuthController;
import com.example.jwtAuth.dtos.requests.JwtRequest;
import com.example.jwtAuth.dtos.responses.JwtResponse;
import com.example.jwtAuth.security.JwtHelper;
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
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper helper;



    public void registerUser(String email, String password) {

        userService.createUser(email, password);
    }


    public String authenticateUserAndGetToken(String email, String password) {
        doAuthenticate(email, password);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return helper.generateToken(userDetails);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Username or Password!!");
        }
    }


}
