package com.devmarquinhos.beanio.controller;

import com.devmarquinhos.beanio.domain.model.CoffeeShop;
import com.devmarquinhos.beanio.domain.model.User;
import com.devmarquinhos.beanio.dto.coffeeShop.CoffeeShopRequest;
import com.devmarquinhos.beanio.dto.coffeeShop.CoffeeShopResponse;
import com.devmarquinhos.beanio.dto.coffeeShop.HighlightRequest;
import com.devmarquinhos.beanio.dto.statistics.CoffeeShopStatisticsResponse;
import com.devmarquinhos.beanio.repository.CoffeeShopRepository;
import com.devmarquinhos.beanio.service.CoffeeShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/coffee-shops")
@RequiredArgsConstructor
public class CoffeeShopController {

    private final CoffeeShopService coffeeShopService;

    @GetMapping
    public ResponseEntity<List<CoffeeShopResponse>> getCoffeeShops(
            @RequestParam(name = "context", required = false) String context) {

        List<CoffeeShopResponse> shops = coffeeShopService.getByContext(context);

        return ResponseEntity.ok(shops);
    }

    @PostMapping
    public ResponseEntity<CoffeeShopResponse> createCoffeeShop(@RequestBody CoffeeShopRequest request,
                                                               @AuthenticationPrincipal User authenticatedUser) {
        CoffeeShopResponse response = coffeeShopService.create(request, authenticatedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoffeeShopResponse> getCoffeeShopById(@PathVariable String id) {
        return ResponseEntity.ok(coffeeShopService.getById(id));
    }

    @PostMapping("/{id}/highlights")
    public ResponseEntity<CoffeeShopResponse> addSpecialtyHighlight(
            @PathVariable UUID id,
            @RequestBody HighlightRequest request,
            @AuthenticationPrincipal User authenticatedUser) {

        CoffeeShopResponse response = coffeeShopService.addSpecialtyHighlight(id, request.imageUrl(), authenticatedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-shop")
    public ResponseEntity<CoffeeShopResponse> getMyShop(@AuthenticationPrincipal User owner) {
        CoffeeShopResponse response = coffeeShopService.getMyShop(owner.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{coffeeShopId}/statistics")
    public ResponseEntity<CoffeeShopStatisticsResponse> getStatistics(
            @PathVariable UUID coffeeShopId,
            @AuthenticationPrincipal User authenticatedUser) {

        CoffeeShopStatisticsResponse stats = coffeeShopService.getStatistics(coffeeShopId, authenticatedUser.getId());
        return ResponseEntity.ok(stats);
    }
}
