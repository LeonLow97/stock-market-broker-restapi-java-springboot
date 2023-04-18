package com.stockmarket.stockmarketapi.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderSubmitDTO {
    
    private String stockTicker;
    private String orderType;
    private int noOfShares;
    private double cost;

}
