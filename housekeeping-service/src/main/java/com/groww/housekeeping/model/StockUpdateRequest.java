package com.groww.housekeeping.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockUpdateRequest {
    @NotNull
    private String name;
    private String symbol;
    private Double openPrice;
    private Double closePrice;
    private Double highPrice;
    private Double lowPrice;
    private Double lastTradedPrice;
    private Long volumeTraded;
    private String marketType;
}
