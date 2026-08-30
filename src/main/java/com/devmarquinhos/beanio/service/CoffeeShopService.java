package com.devmarquinhos.beanio.service;

import com.devmarquinhos.beanio.domain.enums.NoiseLevel;
import com.devmarquinhos.beanio.domain.enums.PricingRange;
import com.devmarquinhos.beanio.domain.enums.VisitContext;
import com.devmarquinhos.beanio.domain.model.CoffeeShop;
import com.devmarquinhos.beanio.domain.model.Review;
import com.devmarquinhos.beanio.domain.model.User;
import com.devmarquinhos.beanio.dto.coffeeShop.CoffeeShopRequest;
import com.devmarquinhos.beanio.dto.coffeeShop.CoffeeShopResponse;
import com.devmarquinhos.beanio.dto.statistics.CoffeeShopStatisticsResponse;
import com.devmarquinhos.beanio.dto.statistics.MonthlyAverage;
import com.devmarquinhos.beanio.exception.BusinessRuleException;
import com.devmarquinhos.beanio.exception.ResourceNotFoundException;
import com.devmarquinhos.beanio.repository.CoffeeShopRepository;
import com.devmarquinhos.beanio.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoffeeShopService {

    private final CoffeeShopRepository repository;
    private final ReviewRepository reviewRepository;

    public List<CoffeeShopResponse> getByContext(String context) {
        List<CoffeeShop> shops = repository.findAll();

        return shops.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CoffeeShopResponse create(CoffeeShopRequest request, User authenticatedUser) {
        CoffeeShop coffeeShop = CoffeeShop.builder()
                .name(request.name())
                .shortDescription(request.shortDescription())
                .address(request.address())
                .district(request.district())
                .city(request.city())
                .openingTime(request.openingTime())
                .closingTime(request.closingTime())
                .pricingRange(request.pricingRange() != null ? request.pricingRange() : PricingRange.MEDIUM)
                .hasWifi(request.hasWifi())
                .hasPowerOutlets(request.hasPowerOutlets())
                .averageNoiseLevel(request.averageNoiseLevel() != null ? request.averageNoiseLevel() : NoiseLevel.MEDIUM)
                .coverImageUrl(request.coverImageUrl())
                .averageScore(0.0)
                .totalReviews(0)
                .owner(authenticatedUser)
                .build();

        CoffeeShop savedShop = repository.save(coffeeShop);

        return mapToResponse(savedShop);
    }

    public CoffeeShopResponse getById(String id) {
        CoffeeShop coffeeShop = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cafeteria não encontrada"));

        return mapToResponse(coffeeShop);
    }

    public CoffeeShopResponse getMyShop(UUID ownerId) {
        CoffeeShop shop = repository.findByOwnerId(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma cafeteria encontrada para este proprietário."));

        return mapToResponse(shop);
    }

    public CoffeeShopResponse addSpecialtyHighlight(UUID coffeeShopId, String imageUrl, User owner) {
        CoffeeShop coffeeShop = repository.findById(coffeeShopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cafeteria não encontrada"));

        if (coffeeShop.getOwner() == null || !coffeeShop.getOwner().getId().equals(owner.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para gerenciar esta cafeteria.");
        }

        if (coffeeShop.getSpecialtyHighlights() == null) {
            coffeeShop.setSpecialtyHighlights(new ArrayList<>());
        }

        if (coffeeShop.getSpecialtyHighlights().size() >= 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você pode cadastrar no máximo 3 fotos como carro-chefe.");
        }

        coffeeShop.getSpecialtyHighlights().add(imageUrl);
        CoffeeShop updatedShop = repository.save(coffeeShop);

        return mapToResponse(updatedShop);
    }

    @Transactional()
    public CoffeeShopStatisticsResponse getStatistics(UUID coffeeShopId, UUID ownerId) {
        CoffeeShop shop = repository.findById(coffeeShopId)
                .orElseThrow(() -> new ResourceNotFoundException("Cafeteria não encontrada."));

        if (!shop.getOwner().getId().equals(ownerId)) {
            throw new BusinessRuleException("Acesso negado. Você não é o proprietário desta cafeteria.");
        }

        List<Review> reviews = reviewRepository.findByCoffeeShopId(coffeeShopId);

        if (reviews.isEmpty()) {
            return new CoffeeShopStatisticsResponse(0.0, 0, Map.of(1,0L, 2,0L, 3,0L, 4,0L, 5,0L), Map.of(), List.of());
        }

        int totalReviews = reviews.size();
        double averageRating = reviews.stream()
                .mapToDouble(Review::getOverallRating)
                .average().orElse(0.0);

        Map<Integer, Long> ratingDistribution = reviews.stream()
                .collect(Collectors.groupingBy(Review::getOverallRating, Collectors.counting()));
        for (int i = 1; i <= 5; i++) ratingDistribution.putIfAbsent(i, 0L);

        Map<String, Map<String, Double>> contextStatistics = new HashMap<>();

        Map<VisitContext, List<Review>> byContext = reviews.stream()
                .filter(r -> r.getVisitContext() != null)
                .collect(Collectors.groupingBy(Review::getVisitContext));

        byContext.forEach((context, ctxReviews) -> {
            Map<String, Double> metrics = new HashMap<>();
            switch (context) {
                case STUDY -> {
                    metrics.put("Silêncio", getAvg(ctxReviews, Review::getSilenceRating));
                    metrics.put("Tomadas", getAvg(ctxReviews, Review::getPowerOutletsRating));
                    metrics.put("Conforto", getAvg(ctxReviews, Review::getSeatComfortRating));
                    metrics.put("Wi-Fi", getAvg(ctxReviews, Review::getWifiRating));
                }
                case REMOTE_WORK -> {
                    metrics.put("Wi-Fi", getAvg(ctxReviews, Review::getWifiRating));
                    metrics.put("Tomadas", getAvg(ctxReviews, Review::getPowerOutletsRating));
                    metrics.put("Conforto", getAvg(ctxReviews, Review::getSeatComfortRating));
                    metrics.put("Longa Permanência", getAvg(ctxReviews, Review::getLongStayToleranceRating));
                }
                case SOCIAL -> {
                    metrics.put("Ambiente", getAvg(ctxReviews, Review::getAmbienceRating));
                    metrics.put("Música", getAvg(ctxReviews, Review::getMusicRating));
                    metrics.put("Conforto", getAvg(ctxReviews, Review::getSeatComfortRating));
                    metrics.put("Privacidade", getAvg(ctxReviews, Review::getPrivacyRating));
                }
                case COFFEE_TASTING -> {
                    metrics.put("Qualidade do Café", getAvg(ctxReviews, Review::getCoffeeQualityRating));
                    metrics.put("Variedade de Métodos", getAvg(ctxReviews, Review::getBrewMethodsVarietyRating));
                    metrics.put("Atendimento", getAvg(ctxReviews, Review::getBaristaServiceRating));
                    metrics.put("Preço Justo", getAvg(ctxReviews, Review::getPriceFairnessRating));
                }
            }
            contextStatistics.put(context.name(), metrics);
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        Map<String, Double> monthlyMap = reviews.stream()
                .filter(r -> r.getCreatedAt() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getCreatedAt().format(formatter),
                        Collectors.averagingDouble(Review::getOverallRating)
                ));

        List<MonthlyAverage> monthlyAverage = monthlyMap.entrySet().stream()
                .map(e -> new MonthlyAverage(e.getKey(), Math.round(e.getValue() * 10.0) / 10.0))
                .sorted(Comparator.comparing(MonthlyAverage::month))
                .toList();

        averageRating = Math.round(averageRating * 10.0) / 10.0;

        return new CoffeeShopStatisticsResponse(averageRating, totalReviews, ratingDistribution, contextStatistics, monthlyAverage);
    }

    private double getAvg(List<Review> reviews, ToIntFunction<Review> mapper) {
        return Math.round(reviews.stream().mapToInt(mapper).average().orElse(0.0) * 10.0) / 10.0;
    }

    private CoffeeShopResponse mapToResponse(CoffeeShop coffeeShop) {
        String score = coffeeShop.getAverageScore() != null
                ? String.format(Locale.US, "%.1f", coffeeShop.getAverageScore())
                : "0.0";

        String location = coffeeShop.getDistrict() + ", " + coffeeShop.getCity();

        return new CoffeeShopResponse(
                coffeeShop.getId(),
                coffeeShop.getName(),
                location,
                score,
                coffeeShop.getCoverImageUrl(),
                coffeeShop.getShortDescription(),
                coffeeShop.isHasWifi(),
                coffeeShop.isHasPowerOutlets(),
                coffeeShop.getSpecialtyHighlights(),
                coffeeShop.getOwner() != null ? coffeeShop.getOwner().getId() : null
        );
    }
}