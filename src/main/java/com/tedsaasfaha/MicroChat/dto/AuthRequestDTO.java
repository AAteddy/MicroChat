
package com.tedsaasfaha.MicroChat.dto;


import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(
        @NotBlank(message = "Email is required.") String email,
        @NotBlank(message = "Password is required") String password
) {
}
//