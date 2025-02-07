package com.tedsaasfaha.MicroChat.config;


import com.tedsaasfaha.MicroChat.model.Role;
import com.tedsaasfaha.MicroChat.model.User;
import com.tedsaasfaha.MicroChat.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitialAdminSetup {

    public InitialAdminSetup(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setName("Initial Admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("securepassword"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }
    }
}
