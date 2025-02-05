package com.tedsaasfaha.MicroChat.dto;

import com.tedsaasfaha.MicroChat.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationDTO(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Email is required.") @Email(message = "Invalid Email Address") String email,
        @NotBlank(message = "Password is required.") @Size(min = 6, message = "Password must be at least 6 characters long") String password,
        Role role // Optional: Assign role during registration (admin-only)
) {
}
