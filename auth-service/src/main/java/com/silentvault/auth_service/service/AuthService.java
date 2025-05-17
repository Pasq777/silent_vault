package com.silentvault.auth_service.service;

import com.silentvault.auth_service.dao.model.User;
import com.silentvault.auth_service.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void register(String username, String email, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username già esistente");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email già in uso");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEnabled(true);

        userRepository.save(user);
    }
}
