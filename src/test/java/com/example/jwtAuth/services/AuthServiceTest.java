package com.example.jwtAuth.services;

import com.example.jwtAuth.dtos.requests.JwtRequest;
import com.example.jwtAuth.dtos.responses.JwtResponse;
import com.example.jwtAuth.models.User;
import com.example.jwtAuth.security.JwtHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private AuthService authService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtHelper jwtHelper;
    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    public void testRegisterUserSuccess() {
        //Arrange
        // Arrange
        String email = "test@example.com";
        String password = "password";

        // Act
        authService.registerUser(email, password);

        // Assert
        // Verify that userService.createUser is called with the correct arguments
        verify(userService, times(1)).createUser(email, password);

        // Verify that other methods are not called
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testRegisterUserFailure() {
        // Arrange
        String email = "test@example.com";
        String password = "password";

        // Mocking userService.createUser to throw an exception
        doThrow(new RuntimeException("Failed to create user"))
                .when(userService).createUser(email, password);

        // Act & Assert
        // Verify that userService.createUser is called with the correct arguments
        // and it throws an exception
        assertThrows(RuntimeException.class, () -> authService.registerUser(email, password));

        // Verify that userService.createUser is called exactly once with the correct arguments
        verify(userService, times(1)).createUser(email, password);

        // Verify that other methods are not called
        verifyNoMoreInteractions(userService);
    }


    @Test
    void testAuthenticateUserAndGetToken() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        String mockToken = "mockedJWTToken";

        UserDetails userDetails = new User(1,email , password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        // Mocking authenticationManager.authenticate() method
        when(authenticationManager.authenticate(authenticationToken)).thenReturn(authenticationToken);

        // Mocking userDetailsService.loadUserByUsername() method
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        // Mocking jwtHelper.generateToken() method
        when(jwtHelper.generateToken(userDetails)).thenReturn(mockToken);

        // Act
        String token = authService.authenticateUserAndGetToken(email, password);

        // Assert
        // Verify that authenticationManager.authenticate() is called with the correct argument
        verify(authenticationManager, times(1)).authenticate(authenticationToken);

        // Verify that userDetailsService.loadUserByUsername() is called with the correct argument
        verify(userDetailsService, times(1)).loadUserByUsername(email);

        // Verify that jwtHelper.generateToken() is called with the correct argument
        verify(jwtHelper, times(1)).generateToken(userDetails);

        // Verify the returned token matches the mocked token
        assertEquals(mockToken, token);
    }

    @Test
    void testAuthenticateUserAndGetTokenWithBadCredentials() {
        // Arrange
        String email = "test@example.com";
        String password = "wrongpassword";

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        // Mocking authenticationManager.authenticate() method to throw BadCredentialsException
        when(authenticationManager.authenticate(authenticationToken)).thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act and Assert
        // Verify that BadCredentialsException is thrown
        assertThrows(BadCredentialsException.class, () -> authService.authenticateUserAndGetToken(email, password));

        // Verify that authenticationManager.authenticate() is called with the correct argument
        verify(authenticationManager, times(1)).authenticate(authenticationToken);

        // Verify that userDetailsService.loadUserByUsername() is not called
        verifyNoInteractions(userDetailsService);

        // Verify that jwtHelper.generateToken() is not called
        verifyNoInteractions(jwtHelper);
    }
}