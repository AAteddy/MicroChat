package com.tedsaasfaha.MicroChat.service;

public interface EmailService {
    void sendPasswordResetEmail(String email, String token);
}
