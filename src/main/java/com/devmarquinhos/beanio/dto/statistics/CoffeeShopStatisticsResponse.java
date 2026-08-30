package com.devmarquinhos.beanio.dto.statistics;

import java.util.List;
import java.util.Map;

public record CoffeeShopStatisticsResponse(
        Double averageRating,
        Integer totalReviews,
        Map<Integer, Long> ratingDistribution,
        Map<String, Map<String, Double>> contextStatistics,
        List<MonthlyAverage> monthlyAverage
) {
}
