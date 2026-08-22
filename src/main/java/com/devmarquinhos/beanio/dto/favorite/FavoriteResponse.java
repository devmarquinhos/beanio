package com.devmarquinhos.beanio.dto.favorite;

import java.time.LocalDateTime;
import java.util.UUID;

public record FavoriteResponse(
        Long id,
        UUID coffeeShopId,
        String coffeeShopName,
        String coverImageUrl,
        LocalDateTime favoritedAt
) {
}
