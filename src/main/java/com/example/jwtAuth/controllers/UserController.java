package com.example.jwtAuth.controllers;

import com.example.jwtAuth.dtos.requests.EmailRequest;
import com.example.jwtAuth.dtos.requests.PasswordRequest;
import com.example.jwtAuth.dtos.responses.UserResponse;
import com.example.jwtAuth.models.User;
import com.example.jwtAuth.repository.UserRepository;
import com.example.jwtAuth.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/change-email")
    public String changeEmail(@Valid @RequestBody EmailRequest emailRequest, Principal principal){
        try{
            String username = principal.getName();
            userService.changeEmail(username, emailRequest.getNewEmail());
            return "Email changed Successfully, Please Login with New Email";
        } catch (IllegalArgumentException e){
            return e.getMessage();
        }

    }

    @PutMapping("/change-password")
    public String changePassword(@Valid @RequestBody PasswordRequest passwordRequest, Principal principal){
        try{
            String username = principal.getName();
            userService.changePassword(username,passwordRequest);
            return "Password changed Successfully";
        } catch (Exception e){
            return e.getMessage();
        }
    }

}
