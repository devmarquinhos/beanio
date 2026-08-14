package com.devmarquinhos.beanio.repository;

import com.devmarquinhos.beanio.domain.enums.VisitContext;
import com.devmarquinhos.beanio.domain.model.CoffeeShop;
import com.devmarquinhos.beanio.domain.model.Review;
import com.devmarquinhos.beanio.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCoffeeShop(CoffeeShop coffeeShop);

    List<Review> findByCoffeeShopAndVisitContext(CoffeeShop coffeeShop, VisitContext visitContext);

    boolean existsByUserAndCoffeeShopAndVisitContextAndCreatedAtBetween(
            User user,
            CoffeeShop coffeeShop,
            VisitContext visitContext,
            LocalDateTime start,
            LocalDateTime end
    );
}
