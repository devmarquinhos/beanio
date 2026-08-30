package com.devmarquinhos.beanio.domain.model;

import com.devmarquinhos.beanio.domain.enums.NoiseLevel;
import com.devmarquinhos.beanio.domain.enums.PricingRange;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "coffee_shops")
public class CoffeeShop {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column()
    private String shortDescription;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private LocalTime openingTime;

    @Column(nullable = false)
    private LocalTime closingTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PricingRange pricingRange = PricingRange.MEDIUM;

    @Column(nullable = false)
    private boolean hasWifi;

    @Column(nullable = false)
    private boolean hasPowerOutlets;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoiseLevel averageNoiseLevel = NoiseLevel.MEDIUM;

    @Column
    private String coverImageUrl;

    @Column(nullable = false, columnDefinition = "Decimal(3,1) default '0.0'")
    private Double averageScore = 0.0;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer totalReviews = 0;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ElementCollection
    @CollectionTable(name = "coffee_shop_highlights", joinColumns = @JoinColumn(name = "coffee_shop_id"))
    @Column(name = "image_url", length = 500)
    private List<String> specialtyHighlights = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
