package com.devmarquinhos.beanio.repository;

import com.devmarquinhos.beanio.domain.model.CoffeeShop;
import com.devmarquinhos.beanio.domain.model.Favorite;
import com.devmarquinhos.beanio.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);

    Optional<Favorite> findByUserAndCoffeeShop(User user, CoffeeShop coffeeShop);

    boolean existsByUserAndCoffeeShop(User user, CoffeeShop coffeeShop);
}
