package com.stockmarket.stockmarketapi.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRegisterDTO {

    public String username;
    public String email;
    @JsonProperty(access = Access.WRITE_ONLY)
    public String password;

}
