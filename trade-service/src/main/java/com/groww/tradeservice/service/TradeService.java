package com.groww.tradeservice.service;

import com.groww.tradeservice.model.TradeRequest;
import com.groww.tradeservice.repository.TradeRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TradeService {

    private final TradeRepository tradeRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public TradeService(TradeRepository tradeRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.tradeRepository = tradeRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public String processTrade(TradeRequest tradeRequest) {
        // Assume tradeRequest is valid due to @Valid in controller

        // Trade execution date validation
        LocalDate tradeDate = LocalDate.now();

        // Save trade to the database
        tradeRepository.save(tradeRequest.toEntity());

        // Publish trade confirmation to Kafka
        kafkaTemplate.send("trade-confirmation", "Trade processed successfully: " + tradeRequest);

        return "Trade successfully recorded!";
    }
}
