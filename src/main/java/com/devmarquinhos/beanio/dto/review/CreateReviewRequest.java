package com.devmarquinhos.beanio.dto.review;
import com.devmarquinhos.beanio.domain.enums.VisitContext;

public record CreateReviewRequest(
        VisitContext context,
        Integer overallRating,
        String comment,

        // study / remote work
        Integer silenceRating,
        Integer powerOutletsRating,
        Integer seatComfortRating,
        Integer wifiRating,
        Integer longStayToleranceRating,

        // social
        Integer ambienceRating,
        Integer musicRating,
        Integer privacyRating,

        // coffee
        Integer coffeeQualityRating,
        Integer brewMethodsVarietyRating,
        Integer baristaServiceRating,
        Integer priceFairnessRating
) {}