package com.example.jwtAuth.security;

import com.example.jwtAuth.models.User;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class JwtAuthFilterTest {

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoFilterInternalWithValidToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String token = "validToken";
        String username = "testuser";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtHelper.getUsernameFromToken(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(new User(1, username, "password"));
        when(jwtHelper.validateToken(eq(token), any(UserDetails.class))).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService, times(1)).loadUserByUsername(username);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithMissingToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // Set up a request without Authorization header
        when(request.getHeader("Authorization")).thenReturn(null);

        // Call the filter
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verify that filter chain is invoked when the token is missing
        verify(filterChain, times(1)).doFilter(request, response);

        // Verify that sendError method is not called when the token is missing
        verify(response, never()).sendError(anyInt(), anyString());
    }

    @Test
    void testDoFilterInternalWithExpiredToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // Set up an expired token
        String expiredToken = "expiredToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + expiredToken);
        when(jwtHelper.isTokenExpired(expiredToken)).thenReturn(true);
        when(jwtHelper.getExpirationDateFromToken(expiredToken)).thenReturn(new Date(System.currentTimeMillis() - 1000)); // Set expiration date in the past

        // Use spy to prevent actual filter chain invocation
        FilterChain filterChainSpy = Mockito.spy(filterChain);

        // Call the filter
        jwtAuthFilter.doFilterInternal(request, response, filterChainSpy);

        // Verify that filter chain is not invoked when the token is expired
        verify(filterChainSpy, never()).doFilter(request, response);

        // Verify that sendError method is called with the correct arguments
        verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
    }
}
