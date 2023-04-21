package com.stockmarket.stockmarketapi.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Data Transfer Object for register user with username, email and password fields.")
public class UserRegisterDTO {

        @Schema(description = "Username for the newly registered user for an account.",
                        example = "leonlow", required = true, name = "username", type = "String")
        public String username;

        @Schema(description = "Email for the newly registered user for an account.",
                        example = "leonlow@email.com", format = "email", required = true,
                        name = "email", type = "String")
        public String email;

        @Schema(description = "Password for the newly registered user for an account.",
                        example = "Password0!", required = true, name = "password")
        @JsonProperty(access = Access.WRITE_ONLY)
        public String password;

}
