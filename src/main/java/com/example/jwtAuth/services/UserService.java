package com.example.jwtAuth.services;

import com.example.jwtAuth.dtos.requests.PasswordRequest;
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

    public void changePassword(String username, PasswordRequest passwordRequest) {
        Optional<User> userOptional = userRepository.findByEmail(username);
        User user = userOptional.get();

        if(!passwordEncoder.matches(passwordRequest.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid current password");
        }

        String newPasswordHash = passwordEncoder.encode(passwordRequest.getNewPassword());
        user.setPassword(newPasswordHash);
        userRepository.save(user);
    }
}
