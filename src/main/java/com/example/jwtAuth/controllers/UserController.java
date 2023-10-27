package com.example.jwtAuth.controllers;

import com.example.jwtAuth.dtos.requests.EmailRequest;
import com.example.jwtAuth.dtos.responses.UserResponse;
import com.example.jwtAuth.models.User;
import com.example.jwtAuth.repository.UserRepository;
import com.example.jwtAuth.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/change-email")
    public String changeEmail(@RequestBody EmailRequest emailRequest, Principal principal){
        try{
            String username = principal.getName();
            userService.changeEmail(username, emailRequest.getNewEmail());
            return "Email changed Successfully, Please Login with New Email";
        } catch (Exception e){
            return e.getMessage();
        }

    }

}
