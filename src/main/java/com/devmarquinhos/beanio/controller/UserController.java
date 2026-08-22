package com.devmarquinhos.beanio.controller;

import com.devmarquinhos.beanio.domain.model.User;
import com.devmarquinhos.beanio.dto.user.UpdateUserRequest;
import com.devmarquinhos.beanio.dto.user.UserResponse;
import com.devmarquinhos.beanio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentProfile(@AuthenticationPrincipal User authenticatedUser) {
        UserResponse response = userService.getUserProfile(authenticatedUser.getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(
            @AuthenticationPrincipal User authenticatedUser,
            @RequestBody UpdateUserRequest request) {

        UserResponse response = userService.updateProfile(authenticatedUser.getId(), request);
        return ResponseEntity.ok(response);
    }
}