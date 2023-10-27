package com.example.jwtAuth.services;

import com.example.jwtAuth.dtos.requests.PasswordRequest;
import com.example.jwtAuth.dtos.responses.UserResponse;
import com.example.jwtAuth.mapper.UserMapper;
import com.example.jwtAuth.models.User;
import com.example.jwtAuth.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
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
        if(EmailValidator.getInstance().isValid(newEmail)) {
            Optional<User> userOptional = userRepository.findByEmail(username);
            userOptional.ifPresent(user -> {
                user.setEmail(newEmail);
                userRepository.save(user);
            });
        }
        else{
            throw new IllegalArgumentException("Invalid email address");
        }
    }


    public void createUser(String email, String password){
        if(EmailValidator.getInstance().isValid(email)){
            User user = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .build();

            userRepository.save(user);
        }
        else{
            throw new IllegalArgumentException("Invalid email address");
        }
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
