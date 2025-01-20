package com.groww.stock_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Stock symbol cannot be null")
    private String symbol;

    @NotNull(message = "Stock name cannot be null")
    private String name;
    private Double openPrice;
    private Double closePrice;
    private Double highPrice;
    private Double lowPrice;
    private Double lastTradedPrice;
    private Double settlementPrice;
    private Double previousClosePrice;
    private Long volumeTraded;
    private String marketType;
    private String securityType;
}
