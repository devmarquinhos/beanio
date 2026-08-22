package com.devmarquinhos.beanio.controller;

import com.devmarquinhos.beanio.dto.coffeeShop.CoffeeShopRequest;
import com.devmarquinhos.beanio.dto.coffeeShop.CoffeeShopResponse;
import com.devmarquinhos.beanio.service.CoffeeShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<CoffeeShopResponse> createCoffeeShop(@RequestBody CoffeeShopRequest request) {
        CoffeeShopResponse response = coffeeShopService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
