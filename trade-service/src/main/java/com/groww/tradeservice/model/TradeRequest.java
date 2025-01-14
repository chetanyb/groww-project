package com.groww.tradeservice.model;

import lombok.Data;

@Data
public class TradeRequest {

    private Long userAccountId;
    private String tradeType;
    private Integer quantity;
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
