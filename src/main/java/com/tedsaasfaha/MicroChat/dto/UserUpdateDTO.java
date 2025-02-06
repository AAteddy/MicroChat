package com.tedsaasfaha.MicroChat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @NotBlank(message = "New Password is required.")
        @Size(min = 6, message = "Password must be at least 6 characters long") String newPassword
) {
}
