
package com.tedsaasfaha.MicroChat.service;


import com.tedsaasfaha.MicroChat.dto.AuthRequestDTO;
import com.tedsaasfaha.MicroChat.dto.AuthResponseDTO;
import com.tedsaasfaha.MicroChat.dto.UserResponseDTO;
import com.tedsaasfaha.MicroChat.dto.UserUpdateDTO;
import com.tedsaasfaha.MicroChat.exception.ResourceNotFoundException;
//import com.tedsaasfaha.MicroChat.mapper.UserMapper;
import com.tedsaasfaha.MicroChat.model.User;
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