package com.stockmarket.stockmarketapi.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Data Transfer Object for a user access token, which is returned upon successful authentication.")
public class UserAccessTokenDTO {

    @Schema(description = "Access Token of the authenticated user upon login.",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2ODIwNDQzNDMsImV4cCI6MTY4MjA1MTU0MywidXNlcklkIjoxLCJlbWFpbCI6Imxlb25sb3dAZW1haWwuY29tIn0.0LCfujAiHOsz0ScDi7-L2cfo76_XccZYOF5uwWxPhJA",
            required = true, name = "accessToken", type = "String")
    public String accessToken;

}
