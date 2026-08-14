package com.devmarquinhos.beanio.repository;

import com.devmarquinhos.beanio.domain.model.CoffeeShop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CoffeeShopRepository extends JpaRepository<CoffeeShop, UUID> {
    List<CoffeeShop> findByCityIgnoreCase(String city);

    List<CoffeeShop> findByCityIgnoreCaseAndDistrictIgnoreCase(String city, String district);

}
