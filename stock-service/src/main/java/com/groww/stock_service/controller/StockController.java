package com.groww.stock_service.controller;

import com.groww.stock_service.model.Stock;
import com.groww.stock_service.service.StockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    @GetMapping("/{id}")
    public Stock getStockById(@PathVariable Long id) {
        return stockService.getStockById(id);
    }

    @PostMapping
    public Stock saveStock(@Valid @RequestBody Stock stock) {
        return stockService.saveStock(stock);
    }

    @PutMapping
    public ResponseEntity<?> updateStock(@Valid @RequestBody Stock stock) {
        return ResponseEntity.ok(stockService.updateStock(stock));
    }
}
