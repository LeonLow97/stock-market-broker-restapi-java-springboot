package com.stockmarket.stockmarketapi.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Data Transfer Object for user login with email and password fields.")
public class UserLoginDTO {

    @Schema(description = "The user's email address used for authentication",
            example = "leonlow@email.com", required = true, name = "email", type = "String",
            format = "email")
    private String email;

    @Schema(description = "The user's password used for authentication", example = "Password0!",
            required = true, name = "password", type = "String")
    private String password;

}
