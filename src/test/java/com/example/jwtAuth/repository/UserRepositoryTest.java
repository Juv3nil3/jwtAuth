package com.example.jwtAuth.repository;

import com.example.jwtAuth.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        assertNotNull(savedUser);
        assertTrue(savedUser.getUserId() > 0);
        assertEquals(email, savedUser.getEmail());
        assertEquals(password, savedUser.getPassword());
    }

    @Test
    void testFindByEmail() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByEmail(email);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(email, foundUser.get().getEmail());
        assertEquals(password, foundUser.get().getPassword());
    }
}