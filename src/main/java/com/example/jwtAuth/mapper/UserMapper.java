package com.example.jwtAuth.mapper;

import com.example.jwtAuth.dtos.responses.UserResponse;
import com.example.jwtAuth.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;
    public static UserResponse UserToUserResponse (User user){
        return UserResponse.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
