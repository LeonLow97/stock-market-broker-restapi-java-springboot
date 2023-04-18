package com.stockmarket.stockmarketapi.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAmountDTO {
    
    @JsonProperty(access = Access.WRITE_ONLY)
    public double amount;

    @JsonProperty(access = Access.READ_ONLY)
    public double balance;

}
