package com.groww.tradeservice.service;

import com.groww.tradeservice.model.TradeRequest;
import com.groww.tradeservice.repository.TradeRepository;
import com.groww.tradeservice.exception.BadRequestException;
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
        // Validate the trade request
        validateTradeRequest(tradeRequest);

        // Trade execution date validation
        LocalDate tradeDate = LocalDate.now();

        // Save trade to the database
        tradeRepository.save(tradeRequest.toEntity());

        // Publish trade confirmation to Kafka
        kafkaTemplate.send("trade-confirmation", "Trade processed successfully: " + tradeRequest);

        return "Trade successfully recorded!";
    }

    private void validateTradeRequest(TradeRequest tradeRequest) {
        if (tradeRequest == null) {
            kafkaTemplate.send("trade-confirmation", "Trade could not be processed: Invalid request");
            throw new BadRequestException("TradeRequest cannot be null.");
        }

        if (tradeRequest.getQuantity() == null || tradeRequest.getQuantity() <= 0) {
            kafkaTemplate.send("trade-confirmation", "Trade could not be processed: Invalid quantity");
            throw new BadRequestException("Quantity must be positive and not null.");
        }

        if (tradeRequest.getTradeType() == null ||
                (!tradeRequest.getTradeType().equalsIgnoreCase("BUY") &&
                        !tradeRequest.getTradeType().equalsIgnoreCase("SELL"))) {
            kafkaTemplate.send("trade-confirmation", "Trade could not be processed: Invalid trade type");
            throw new BadRequestException("Invalid trade type. Must be BUY or SELL.");
        }

        if (tradeRequest.getStockId() == null || tradeRequest.getStockId() <= 0) {
            kafkaTemplate.send("trade-confirmation", "Trade could not be processed: Invalid stock ID");
            throw new BadRequestException("Stock ID must be positive and not null.");
        }

    }
}
