package com.example.jwtAuth.services;

import com.example.jwtAuth.models.User;
import com.example.jwtAuth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User getProfile(String email){
        Optional<User> userOptional = userRepository.findByUsername(email);
        User response = userOptional.get();
        return response;
    }

    public User createUser(User user){
        return userRepository.save(user);
    }
}
