package com.devmarquinhos.beanio.service;

import com.devmarquinhos.beanio.domain.model.CoffeeShop;
import com.devmarquinhos.beanio.domain.model.User;
import com.devmarquinhos.beanio.dto.coffeeShop.CoffeeShopRequest;
import com.devmarquinhos.beanio.dto.coffeeShop.CoffeeShopResponse;
import com.devmarquinhos.beanio.repository.CoffeeShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoffeeShopService {

    private final CoffeeShopRepository repository;

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
                .pricingRange(request.pricingRange())
                .hasWifi(request.hasWifi())
                .hasPowerOutlets(request.hasPowerOutlets())
                .averageNoiseLevel(request.averageNoiseLevel())
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