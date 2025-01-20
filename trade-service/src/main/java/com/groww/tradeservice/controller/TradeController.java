package com.groww.tradeservice.controller;

import com.groww.tradeservice.model.TradeRequest;
import com.groww.tradeservice.service.TradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/trade")
@Tag(name = "Trade", description = "Trade management operations")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @Operation(
            summary = "Record a new trade",
            description = "Records a new trade transaction in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Trade recorded successfully",
                    content = @Content(schema = @Schema(implementation = Object.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid trade request"
            )
    })
    @PostMapping
    public ResponseEntity<?> recordTrade(@Valid @RequestBody TradeRequest tradeRequest) {
        return ResponseEntity.ok(tradeService.processTrade(tradeRequest));
    }
}