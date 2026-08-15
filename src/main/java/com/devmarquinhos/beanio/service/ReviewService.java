package com.devmarquinhos.beanio.service;

import com.devmarquinhos.beanio.domain.enums.VisitContext;
import com.devmarquinhos.beanio.domain.model.CoffeeShop;
import com.devmarquinhos.beanio.domain.model.Review;
import com.devmarquinhos.beanio.domain.model.User;
import com.devmarquinhos.beanio.dto.review.ReviewResponse;
import com.devmarquinhos.beanio.dto.review.CreateReviewRequest;
import com.devmarquinhos.beanio.repository.CoffeeShopRepository;
import com.devmarquinhos.beanio.repository.ReviewRepository;
import com.devmarquinhos.beanio.exception.BusinessRuleException;
import com.devmarquinhos.beanio.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CoffeeShopRepository coffeeShopRepository;

    @Transactional
    public ReviewResponse createReview(UUID coffeeShopId, User user, CreateReviewRequest request) {
        CoffeeShop coffeeShop = coffeeShopRepository.findById(coffeeShopId)
                .orElseThrow(() -> new ResourceNotFoundException("Cafeteria não encontrada."));

        validateBasicRules(request);
        validateContextSubratings(request);
        validateOneReviewPerDay(user, coffeeShop, request.context());

        Review review = buildReview(user, coffeeShop, request);
        Review savedReview = reviewRepository.save(review);

        return mapToResponse(savedReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> listReviews(UUID coffeeShopId, VisitContext context) {
        CoffeeShop coffeeShop = coffeeShopRepository.findById(coffeeShopId)
                .orElseThrow(() -> new ResourceNotFoundException("Cafeteria não encontrada."));

        List<Review> reviews = (context != null)
                ? reviewRepository.findByCoffeeShopAndVisitContext(coffeeShop, context)
                : reviewRepository.findByCoffeeShop(coffeeShop);

        return reviews.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void validateBasicRules(CreateReviewRequest request) {
        if (request.context() == null) {
            throw new BusinessRuleException("O contexto da visita (VisitContext) é obrigatório.");
        }
        validateRating(request.overallRating(), "Nota geral");
    }

    private void validateContextSubratings(CreateReviewRequest req) {
        switch (req.context()) {
            case STUDY -> {
                validateRating(req.silenceRating(), "Nota de Silêncio");
                validateRating(req.powerOutletsRating(), "Nota de Tomadas");
                validateRating(req.seatComfortRating(), "Nota de Conforto");
                validateRating(req.wifiRating(), "Nota de Wi-Fi");
            }
            case REMOTE_WORK -> {
                validateRating(req.wifiRating(), "Nota de Wi-Fi");
                validateRating(req.powerOutletsRating(), "Nota de Tomadas");
                validateRating(req.seatComfortRating(), "Nota de Conforto");
                validateRating(req.longStayToleranceRating(), "Nota de Tolerância a longa estadia");
            }
            case SOCIAL -> {
                validateRating(req.ambienceRating(), "Nota de Ambiente");
                validateRating(req.musicRating(), "Nota de Música");
                validateRating(req.seatComfortRating(), "Nota de Conforto");
                validateRating(req.privacyRating(), "Nota de Privacidade");
            }
            case COFFEE_TASTING -> {
                validateRating(req.coffeeQualityRating(), "Nota de Qualidade do Café");
                validateRating(req.brewMethodsVarietyRating(), "Nota de Variedade de Métodos");
                validateRating(req.baristaServiceRating(), "Nota de Atendimento do Barista");
                validateRating(req.priceFairnessRating(), "Nota de Preço Justo");
            }
        }
    }

    private void validateRating(Integer rating, String fieldName) {
        if (rating == null || rating < 0 || rating > 5) {
            throw new BusinessRuleException(fieldName + " É obrigatório avaliar de 0 a 5.");
        }
    }

    private void validateOneReviewPerDay(User user, CoffeeShop coffeeShop, VisitContext context) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        boolean alreadyReviewed = reviewRepository.existsByUserAndCoffeeShopAndVisitContextAndCreatedAtBetween(
                user, coffeeShop, context, startOfDay, endOfDay
        );

        if (alreadyReviewed) {
            throw new BusinessRuleException("Você já avaliou a cafeteria hoje.");
        }
    }

    private Review buildReview(User user, CoffeeShop coffeeShop, CreateReviewRequest request) {
        return Review.builder()
                .user(user)
                .coffeeShop(coffeeShop)
                .visitContext(request.context())
                .overallRating(request.overallRating())
                .comment(request.comment())
                .silenceRating(request.silenceRating())
                .powerOutletsRating(request.powerOutletsRating())
                .seatComfortRating(request.seatComfortRating())
                .wifiRating(request.wifiRating())
                .longStayToleranceRating(request.longStayToleranceRating())
                .ambienceRating(request.ambienceRating())
                .musicRating(request.musicRating())
                .privacyRating(request.privacyRating())
                .coffeeQualityRating(request.coffeeQualityRating())
                .brewMethodsVarietyRating(request.brewMethodsVarietyRating())
                .baristaServiceRating(request.baristaServiceRating())
                .priceFairnessRating(request.priceFairnessRating())
                .build();
    }

    private ReviewResponse mapToResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getUser().getId(),
                review.getUser().getName(),
                review.getVisitContext(),
                review.getOverallRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getSilenceRating(),
                review.getPowerOutletsRating(),
                review.getSeatComfortRating(),
                review.getWifiRating(),
                review.getLongStayToleranceRating(),
                review.getAmbienceRating(),
                review.getMusicRating(),
                review.getPrivacyRating(),
                review.getCoffeeQualityRating(),
                review.getBrewMethodsVarietyRating(),
                review.getBaristaServiceRating(),
                review.getPriceFairnessRating()
        );
    }
}