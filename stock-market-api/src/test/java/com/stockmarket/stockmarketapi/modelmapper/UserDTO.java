package com.stockmarket.stockmarketapi.modelmapper;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long userId;
    private String username;
    private String password;
    private String email;
    private Double balance;
    private int isActive;
    private LocalDateTime addedDate;
    private LocalDateTime updatedDate;
    
}
