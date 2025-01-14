package com.groww.tradeservice.controller;

import com.groww.tradeservice.model.TradeRequest;
import com.groww.tradeservice.service.TradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trade")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping
    public ResponseEntity<?> recordTrade(@RequestBody TradeRequest tradeRequest) {
        return ResponseEntity.ok(tradeService.processTrade(tradeRequest));
    }
}
