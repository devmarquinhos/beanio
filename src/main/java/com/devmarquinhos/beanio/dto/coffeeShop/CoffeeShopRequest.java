package com.devmarquinhos.beanio.dto.coffeeShop;

import com.devmarquinhos.beanio.domain.enums.NoiseLevel;
import com.devmarquinhos.beanio.domain.enums.PricingRange;

import java.time.LocalTime;

public record CoffeeShopRequest(
        String name,
        String shortDescription,
        String address,
        String district,
        String city,
        LocalTime openingTime,
        LocalTime closingTime,
        PricingRange pricingRange,
        boolean hasWifi,
        boolean hasPowerOutlets,
        NoiseLevel averageNoiseLevel,
        String coverImageUrl
) {}
