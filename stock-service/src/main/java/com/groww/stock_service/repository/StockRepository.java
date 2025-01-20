package com.groww.stock_service.repository;

import com.groww.stock_service.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    // Custom query to find a stock by its symbol
    Optional<Stock> findBySymbol(String symbol);
}
