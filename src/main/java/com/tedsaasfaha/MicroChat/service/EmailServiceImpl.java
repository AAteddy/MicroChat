package com.tedsaasfaha.MicroChat.service;


import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendPasswordResetEmail(String email, String token) {
        // For now, just log the email and token
        System.out.println("Sending Password reset email to: " + email);
        System.out.println("Reset Token: " + token);

        // In a real application, you would send an email with a link like:
        // http://yourapp.com/reset-password?token=abc123
    }
}
