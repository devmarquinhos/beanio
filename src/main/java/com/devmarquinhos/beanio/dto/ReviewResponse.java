package com.devmarquinhos.beanio.dto;

import com.devmarquinhos.beanio.domain.enums.VisitContext;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponse(
        Long id,
        UUID userId,
        String userName,
        VisitContext context,
        Integer overallRating,
        String comment,
        LocalDateTime createdAt,

        Integer silenceRating,
        Integer powerOutletsRating,
        Integer seatComfortRating,
        Integer wifiRating,
        Integer longStayToleranceRating,

        Integer ambienceRating,
        Integer musicRating,
        Integer privacyRating,

        Integer coffeeQualityRating,
        Integer brewMethodsVarietyRating,
        Integer baristaServiceRating,
        Integer priceFairnessRating
) {}
