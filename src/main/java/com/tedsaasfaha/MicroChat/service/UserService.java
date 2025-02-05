package com.tedsaasfaha.MicroChat.service;


import com.tedsaasfaha.MicroChat.dto.UserRegistrationDTO;
import com.tedsaasfaha.MicroChat.dto.UserUpdateDTO;
import com.tedsaasfaha.MicroChat.exception.ResourceNotFoundException;
import com.tedsaasfaha.MicroChat.model.Role;
import com.tedsaasfaha.MicroChat.model.User;
import com.tedsaasfaha.MicroChat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserRegistrationDTO registrationDTO) {
        User user = new User();
        user.setName(registrationDTO.name());
        user.setEmail(registrationDTO.email());
        user.setPassword(passwordEncoder.encode(registrationDTO.password()));
        user.setRole(registrationDTO.role() != null ? registrationDTO.role() : Role.USER); // Default User Role

        return userRepository.save(user);
    }

    public User updateProfile(UserUpdateDTO updateDTO, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        user.setPassword(passwordEncoder.encode(updateDTO.newPassword()));
        return userRepository.save(user);
    }
}
