package com.tedsaasfaha.MicroChat.dto;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken) {
}
