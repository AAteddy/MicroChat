
package com.tedsaasfaha.MicroChat.service;


import com.tedsaasfaha.MicroChat.dto.AuthRequestDTO;
import com.tedsaasfaha.MicroChat.dto.AuthResponseDTO;
import com.tedsaasfaha.MicroChat.dto.UserResponseDTO;
import com.tedsaasfaha.MicroChat.dto.UserUpdateDTO;
import com.tedsaasfaha.MicroChat.exception.ResourceNotFoundException;
import com.tedsaasfaha.MicroChat.model.PasswordResetToken;
import com.tedsaasfaha.MicroChat.model.User;
import com.tedsaasfaha.MicroChat.repository.PasswordResetTokenRepository;
import com.tedsaasfaha.MicroChat.repository.UserRepository;
import com.tedsaasfaha.MicroChat.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService;


    public boolean isExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserResponseDTO updateProfile(UserUpdateDTO updateDTO, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        user.setPassword(passwordEncoder.encode(updateDTO.newPassword()));


        return toUserResponseDTO(userRepository.save(user));
    }

    public AuthResponseDTO createAuthenticationToken(AuthRequestDTO authRequestDTO) throws Exception {
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
        final String accessToken = jwtUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return new AuthResponseDTO(accessToken, refreshToken);
    }

    public AuthResponseDTO createAuthRefreshToken(AuthResponseDTO responseDTO) throws Exception {
        String refreshToken = responseDTO.refreshToken();

        // Validate refresh token
        if (jwtUtil.validateToken(refreshToken))
            throw new Exception("Invalid token.");

        // extract username
        String username = jwtUtil.extractUsername(refreshToken);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Generate a new Access Token
        String accessToken = jwtUtil.generateAccessToken(userDetails);

        return new AuthResponseDTO(accessToken, refreshToken);
    }

    @Override
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAllUsers(pageable);

        return users.map(this::toUserResponseDTO);
    }

    @Override
    public Page<UserResponseDTO> getAllActiveUsers(Pageable pageable) {
        Page<User> activeUsers = userRepository.findAllActiveUsers(pageable);

        return activeUsers.map(this::toUserResponseDTO);
    }

    @Override
    public void removeUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with Id: " + userId));

        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found,"));

        // Generate a unique token
        String token = UUID.randomUUID().toString();

        // Create and save a token
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(resetToken);

        // Send the token via email
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Override
    public void completePasswordReset(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token."));

        // Check if the token has expired
        if (resetToken.isExpired()) {
            throw new ResourceNotFoundException("Token has expired.");
        }

        // Update the user's password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Delete the token
        passwordResetTokenRepository.delete(resetToken);
    }


    private UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
//