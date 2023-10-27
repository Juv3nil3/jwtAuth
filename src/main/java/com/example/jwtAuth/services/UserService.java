package com.example.jwtAuth.services;

import com.example.jwtAuth.models.User;
import com.example.jwtAuth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getProfile(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        User response = userOptional.get();
        return response;
    }

    public void createUser(String email, String password){
        User user = User.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                    .build();

        userRepository.save(user);
    }
}
