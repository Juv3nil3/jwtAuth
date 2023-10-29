package com.example.jwtAuth.dtos.requests;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequest {

    @NotBlank(message = "Current password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String newPassword;
}
