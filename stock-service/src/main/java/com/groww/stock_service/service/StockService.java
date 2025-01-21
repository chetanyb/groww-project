package com.groww.stock_service.service;

import com.groww.stock_service.model.Stock;
import com.groww.stock_service.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock getStockById(Long id) {
        return stockRepository.findById(id).orElseThrow(() -> new RuntimeException("Stock not found"));
    }

    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public Stock updateStock(Stock stock) {
        Stock existingStock = stockRepository.findBySymbol(stock.getSymbol())
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        existingStock.setOpenPrice(stock.getOpenPrice());
        existingStock.setClosePrice(stock.getClosePrice());
        existingStock.setHighPrice(stock.getHighPrice());
        existingStock.setLowPrice(stock.getLowPrice());
        existingStock.setLastTradedPrice(stock.getLastTradedPrice());
        existingStock.setVolumeTraded(stock.getVolumeTraded());
        existingStock.setMarketType(stock.getMarketType());
        return stockRepository.save(existingStock);
    }
}
