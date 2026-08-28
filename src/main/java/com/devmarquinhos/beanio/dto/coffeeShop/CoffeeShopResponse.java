package com.devmarquinhos.beanio.dto.coffeeShop;

import java.util.List;
import java.util.UUID;

public record CoffeeShopResponse(
        UUID id,
        String name,
        String location,
        String score,
        String coverImageUrl,
        String shortDescription,
        boolean hasWifi,
        boolean hasPowerOutlets,
        List<String> specialtyHighlights,
        UUID ownerId) {}