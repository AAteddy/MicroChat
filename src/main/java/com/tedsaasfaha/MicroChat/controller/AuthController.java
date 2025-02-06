package com.tedsaasfaha.MicroChat.controller;


import com.tedsaasfaha.MicroChat.dto.AuthRequestDTO;
import com.tedsaasfaha.MicroChat.dto.AuthResponseDTO;
import com.tedsaasfaha.MicroChat.dto.UserRegistrationDTO;
import com.tedsaasfaha.MicroChat.model.User;
import com.tedsaasfaha.MicroChat.service.UserService;
import com.tedsaasfaha.MicroChat.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserRegistrationDTO registrationDTO
            ) {

        if (userService.isExist(registrationDTO.email())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User with the email already exist.");
        }

        User user = userService.registerUser(registrationDTO);
        return ResponseEntity.ok("Successfully Registered User: " + user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(
            @RequestBody AuthRequestDTO authRequestDTO
            ) throws Exception {

        AuthResponseDTO response = userService.createAuthenticationToken(authRequestDTO);
        return ResponseEntity.ok(response);
    }

    // todo - refresh-token method endpoint

}
