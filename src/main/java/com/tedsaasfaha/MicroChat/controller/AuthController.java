
package com.tedsaasfaha.MicroChat.controller;


import com.tedsaasfaha.MicroChat.config.TokenBlacklist;
import com.tedsaasfaha.MicroChat.dto.AuthRequestDTO;
import com.tedsaasfaha.MicroChat.dto.AuthResponseDTO;
import com.tedsaasfaha.MicroChat.dto.UserRegistrationDTO;
import com.tedsaasfaha.MicroChat.model.Role;
import com.tedsaasfaha.MicroChat.model.User;
import com.tedsaasfaha.MicroChat.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(
            @Valid @RequestBody UserRegistrationDTO registrationDTO
            ) {

        if (userService.isExist(registrationDTO.email())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User with the email already exist.");
        }

        User user = new User();
        user.setName(registrationDTO.name());
        user.setEmail(registrationDTO.email());
        user.setPassword(registrationDTO.password());
        user.setRole(Role.USER); // Default User Role

        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok("Successfully Registered User: " + savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(
            @RequestBody AuthRequestDTO authRequestDTO
            ) throws Exception {

        AuthResponseDTO response = userService.createAuthenticationToken(authRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(
            @RequestHeader("Authorization") String token
    ) {

        String jwt = token.substring(7);
        tokenBlacklist.blacklistToken(jwt);
        return ResponseEntity.ok("Logged out successfully.");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDTO> refreshToken(
            @RequestBody AuthResponseDTO responseDTO
    ) throws Exception {

        AuthResponseDTO response = userService.createAuthRefreshToken(responseDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestParam String email
    ) {

        userService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset email sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword
    ) {

        userService.completePasswordReset(token, newPassword);
        return ResponseEntity.ok("Password reset successfully.");
    }
}
//