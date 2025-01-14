package com.groww.tradeservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userAccountId;
    private String tradeType;
    private Integer quantity;
    private Long stockId;
}
