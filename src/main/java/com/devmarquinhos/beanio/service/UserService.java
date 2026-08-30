package com.devmarquinhos.beanio.service;

import com.devmarquinhos.beanio.domain.enums.UserRole;
import com.devmarquinhos.beanio.domain.model.User;
import com.devmarquinhos.beanio.dto.user.*;
import com.devmarquinhos.beanio.repository.UserRepository;
import com.devmarquinhos.beanio.exception.BusinessRuleException;
import com.devmarquinhos.beanio.exception.ResourceNotFoundException;
import com.devmarquinhos.beanio.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessRuleException("Este e-mail já está em uso.");
        }

        User newUser = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .city(request.city())
                .role(request.role() != null ? request.role() : UserRole.USER)
                .build();

        User savedUser = userRepository.save(newUser);
        return mapToResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessRuleException("Credenciais inválidas."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessRuleException("Credenciais inválidas.");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(token, mapToResponse(user));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        return mapToResponse(user);
    }

    @Transactional
    public UserResponse updateProfile(UUID userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        user.setName(request.name());
        user.setCity(request.city());

        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCity(),
                user.getRole()
        );
    }
}