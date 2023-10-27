package com.example.jwtAuth.services;

import com.example.jwtAuth.dtos.responses.UserResponse;
import com.example.jwtAuth.mapper.UserMapper;
import com.example.jwtAuth.models.User;
import com.example.jwtAuth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void changeEmail(String username, String newEmail) {
        Optional<User> userOptional = userRepository.findByEmail(username);
        userOptional.ifPresent(user -> {
            user.setEmail(newEmail);
            userRepository.save(user);
        });
    }


    public void createUser(String email, String password){
        User user = User.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                    .build();

        userRepository.save(user);
    }
}
