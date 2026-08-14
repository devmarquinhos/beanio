package com.devmarquinhos.beanio.controller;

import com.devmarquinhos.beanio.domain.enums.VisitContext;
import com.devmarquinhos.beanio.domain.model.User;
import com.devmarquinhos.beanio.dto.ReviewResponse;
import com.devmarquinhos.beanio.dto.review.CreateReviewRequest;
import com.devmarquinhos.beanio.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/coffee-shops/{coffeeShopId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable UUID coffeeShopId,
            @AuthenticationPrincipal User authenticatedUser,
            @RequestBody CreateReviewRequest request) {

        ReviewResponse response = reviewService.createReview(coffeeShopId, authenticatedUser, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> listReviews(
            @PathVariable UUID coffeeShopId,
            @RequestParam(required = false) VisitContext context) {

        List<ReviewResponse> responses = reviewService.listReviews(coffeeShopId, context);
        return ResponseEntity.ok(responses);
    }
}