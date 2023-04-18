package com.stockmarket.stockmarketapi.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginDTO {
    
    private String email;
    private String password;

}
