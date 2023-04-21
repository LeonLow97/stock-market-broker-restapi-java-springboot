package com.stockmarket.stockmarketapi.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Data Transfer Object for submitting the necessary fields needed to create an order by the user.")
public class OrderSubmitDTO {

    @Schema(description = "Stock ticker of the stock the user wants to buy/sell.", example = "AAPL",
            required = true)
    private String stockTicker;

    @Schema(description = "Order type refers to the action which can only be 'BUY' or 'SELL'",
            example = "BUY", allowableValues = {"BUY", "SELL"}, required = true)
    private String orderType;

    @Schema(description = "No of shares the user wants to BUY/SELL in the order.", example = "60",
            required = true)
    private int noOfShares;

    @Schema(description = "Price of the stock the user is willing to BUY/SELL in the order.",
            example = "149.86", required = true)
    private double cost;

}
