package com.stockmarket.stockmarketapi.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Data Transfer Object for user to deposit/withdraw funds from the account.")
public class UserAmountDTO {

        @Schema(description = "Amount specified by the user to deposit or withdraw to/from the account",
                        example = "1000.0", required = true, name = "amount", type = "double")
        @JsonProperty(access = Access.WRITE_ONLY)
        public double amount;

        @Schema(description = "Shows the balance in the user's account.", example = "2000.0",
                        required = true, name = "balance", type = "double")
        @JsonProperty(access = Access.READ_ONLY)
        public double balance;

}
