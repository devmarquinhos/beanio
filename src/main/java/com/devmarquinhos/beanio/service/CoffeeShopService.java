package com.devmarquinhos.beanio.service;

import com.devmarquinhos.beanio.domain.model.CoffeeShop;
import com.devmarquinhos.beanio.dto.coffeeShop.CoffeeShopRequest;
import com.devmarquinhos.beanio.dto.coffeeShop.CoffeeShopResponse;
import com.devmarquinhos.beanio.repository.CoffeeShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoffeeShopService {

    private final CoffeeShopRepository repository;

    public List<CoffeeShopResponse> getByContext(String context) {
        // TODO: criar uma Specification ou Query no repository para filtros inteligentes
        List<CoffeeShop> shops = repository.findAll();

        return shops.stream()
                .map(shop -> new CoffeeShopResponse(
                        shop.getId(),
                        shop.getName(),
                        shop.getDistrict() + ", " + shop.getCity(),
                        String.format(Locale.US, "%.1f", shop.getAverageScore()),
                        shop.getCoverImageUrl()
                ))
                .toList();
    }

    public CoffeeShopResponse create(CoffeeShopRequest request) {
        CoffeeShop coffeeShop = CoffeeShop.builder()
                .name(request.name())
                .shortDescription(request.shortDescription())
                .address(request.address())
                .district(request.district())
                .city(request.city())
                .openingTime(request.openingTime())
                .closingTime(request.closingTime())
                .pricingRange(request.pricingRange())
                .hasWifi(request.hasWifi())
                .hasPowerOutlets(request.hasPowerOutlets())
                .averageNoiseLevel(request.averageNoiseLevel())
                .coverImageUrl(request.coverImageUrl())
                .averageScore(0.0)
                .totalReviews(0)
                .build();

        CoffeeShop savedShop = repository.save(coffeeShop);

        return new CoffeeShopResponse(
                savedShop.getId(),
                savedShop.getName(),
                savedShop.getDistrict() + ", " + savedShop.getCity(),
                "0.0",
                savedShop.getCoverImageUrl()
        );
    }
}
