package com.tedsaasfaha.MicroChat.controller;


import com.tedsaasfaha.MicroChat.dto.AuthRequestDTO;
import com.tedsaasfaha.MicroChat.dto.AuthResponseDTO;
import com.tedsaasfaha.MicroChat.dto.UserRegistrationDTO;
import com.tedsaasfaha.MicroChat.model.User;
import com.tedsaasfaha.MicroChat.service.UserService;
import com.tedsaasfaha.MicroChat.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserRegistrationDTO registrationDTO
            ) {

        User user = userService.registerUser(registrationDTO);
        return ResponseEntity.ok("User Registered Successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody AuthRequestDTO authRequestDTO
            ) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequestDTO.email(),
                            authRequestDTO.password())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequestDTO.email());
        final String jwt = jwtUtil.generateAccessToken(userDetails);

        return ResponseEntity.ok(new AuthResponseDTO(jwt));
    }
}
