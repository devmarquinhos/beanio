package com.devmarquinhos.beanio.controller;

import com.devmarquinhos.beanio.domain.model.User;
import com.devmarquinhos.beanio.dto.favorite.FavoriteResponse;
import com.devmarquinhos.beanio.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/coffee-shops/{coffeeShopId}/favorites")
    public ResponseEntity<Void> addFavorite(
            @AuthenticationPrincipal User authenticatedUser,
            @PathVariable UUID coffeeShopId) {

        favoriteService.addFavorite(authenticatedUser, coffeeShopId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/coffee-shops/{coffeeShopId}/favorites")
    public ResponseEntity<Void> removeFavorite(
            @AuthenticationPrincipal User authenticatedUser,
            @PathVariable UUID coffeeShopId) {

        favoriteService.removeFavorite(authenticatedUser, coffeeShopId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/me/favorites")
    public ResponseEntity<List<FavoriteResponse>> getMyFavorites(
            @AuthenticationPrincipal User authenticatedUser) {

        List<FavoriteResponse> responses = favoriteService.listUserFavorites(authenticatedUser);
        return ResponseEntity.ok(responses);
    }
}