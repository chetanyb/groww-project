package com.groww.tradeservice.repository;

import com.groww.tradeservice.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}
