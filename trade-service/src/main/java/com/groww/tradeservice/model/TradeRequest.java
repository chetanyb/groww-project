package com.groww.tradeservice.model;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
public class TradeRequest {

    @NotNull(message = "User account ID must be provided")
    private Long userAccountId;

    @NotNull(message = "Trade type must be provided")
    @Pattern(regexp = "BUY|SELL", message = "Trade type must be BUY or SELL")
    private String tradeType;

    @NotNull(message = "Quantity must be provided")
    @Min(value = 1, message = "Quantity must be positive")
    private Integer quantity;

    @NotNull(message = "Stock ID must be provided")
    @Min(value = 1, message = "Stock ID must be positive")
    private Long stockId;

    public Trade toEntity() {
        Trade trade = new Trade();
        trade.setUserAccountId(this.userAccountId);
        trade.setTradeType(this.tradeType);
        trade.setQuantity(this.quantity);
        trade.setStockId(this.stockId);
        return trade;
    }
}
