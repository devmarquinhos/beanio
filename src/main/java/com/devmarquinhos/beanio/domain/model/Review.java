package com.devmarquinhos.beanio.domain.model;

import com.devmarquinhos.beanio.domain.enums.VisitContext;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coffee_shop_id")
    private CoffeeShop coffeeShop;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VisitContext visitContext;

    @Column(nullable = false)
    private Integer overallRating;

    @Column(length = 1000)
    private String comment;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // rates based in visit context
    private Integer silenceRating;
    private Integer powerOutletsRating;
    private Integer seatComfortRating;
    private Integer wifiRating;
    private Integer longStayToleranceRating;

    private Integer ambienceRating;
    private Integer musicRating;
    private Integer privacyRating;

    private Integer coffeeQualityRating;
    private Integer brewMethodsVarietyRating;
    private Integer baristaServiceRating;
    private Integer priceFairnessRating;

}
