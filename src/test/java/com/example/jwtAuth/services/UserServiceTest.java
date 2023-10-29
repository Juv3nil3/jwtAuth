package com.example.jwtAuth.services;

import com.example.jwtAuth.dtos.requests.PasswordRequest;
import com.example.jwtAuth.models.User;
import com.example.jwtAuth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testChangeEmailSuccess() {

        User user = new User();
        user.setEmail("old@example.com");

        when(userRepository.findByEmail("old@example.com")).thenReturn(Optional.of(user));

        String newEmail = "new@example.com";
        userService.changeEmail("old@example.com", newEmail);

        assertEquals(newEmail, user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testChangePasswordSuccess() {
        String currentPassword = "oldPassword";
        String newPassword = "newPassword";

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode(currentPassword));

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);

        PasswordRequest passwordRequest = new PasswordRequest();
        passwordRequest.setCurrentPassword(currentPassword);
        passwordRequest.setNewPassword(newPassword);

        userService.changePassword("test@example.com", passwordRequest);

        String expectedNewPasswordHash = passwordEncoder.encode(newPassword);
        assertEquals(expectedNewPasswordHash, user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testChangePasswordInvalidCurrentPassword() {
        String currentPassword = "wrongPassword";
        String newPassword = "newPassword";

        User user = new User();
        user.setEmail("test@example.com");
        String originalPasswordHash = passwordEncoder.encode("correctPassword");
        user.setPassword(originalPasswordHash);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(currentPassword, originalPasswordHash)).thenReturn(false);

        PasswordRequest passwordRequest = new PasswordRequest();
        passwordRequest.setCurrentPassword(currentPassword);
        passwordRequest.setNewPassword(newPassword);

        assertThrows(IllegalArgumentException.class, () -> userService.changePassword("test@example.com", passwordRequest));

        // Ensure that user password is not changed
        assertEquals(originalPasswordHash, user.getPassword());
        verify(userRepository, never()).save(user);
    }

}