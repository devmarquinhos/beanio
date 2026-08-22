package com.devmarquinhos.beanio.service;

import com.devmarquinhos.beanio.domain.model.CoffeeShop;
import com.devmarquinhos.beanio.domain.model.Favorite;
import com.devmarquinhos.beanio.domain.model.User;
import com.devmarquinhos.beanio.dto.favorite.FavoriteResponse;
import com.devmarquinhos.beanio.repository.CoffeeShopRepository;
import com.devmarquinhos.beanio.repository.FavoriteRepository;
import com.devmarquinhos.beanio.exception.BusinessRuleException;
import com.devmarquinhos.beanio.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final CoffeeShopRepository coffeeShopRepository;

    @Transactional
    public void addFavorite(User user, UUID coffeeShopId) {
        CoffeeShop coffeeShop = coffeeShopRepository.findById(coffeeShopId)
                .orElseThrow(() -> new ResourceNotFoundException("Cafeteria não encontrada."));

        if (favoriteRepository.existsByUserAndCoffeeShop(user, coffeeShop)) {
            throw new BusinessRuleException("Esta cafeteria já está nos seus favoritos.");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .coffeeShop(coffeeShop)
                .build();

        favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(User user, UUID coffeeShopId) {
        CoffeeShop coffeeShop = coffeeShopRepository.findById(coffeeShopId)
                .orElseThrow(() -> new ResourceNotFoundException("Cafeteria não encontrada."));

        Favorite favorite = favoriteRepository.findByUserAndCoffeeShop(user, coffeeShop)
                .orElseThrow(() -> new ResourceNotFoundException("Esta cafeteria não está nos seus favoritos."));

        favoriteRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> listUserFavorites(User user) {
        List<Favorite> favorites = favoriteRepository.findByUser(user);

        return favorites.stream()
                .map(fav -> new FavoriteResponse(
                        fav.getId(),
                        fav.getCoffeeShop().getId(),
                        fav.getCoffeeShop().getName(),
                        fav.getCoffeeShop().getCoverImageUrl(),
                        fav.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}