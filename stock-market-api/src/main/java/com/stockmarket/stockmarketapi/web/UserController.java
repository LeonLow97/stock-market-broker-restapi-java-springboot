package com.stockmarket.stockmarketapi.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.stockmarket.stockmarketapi.Constants;
import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User Controller",
        description = "To sign up for an account and log in with the provided login credentials. Provides endpoint for withdrawing and depositing cash into the created account.")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(summary = "Login account.",
            description = "Provides a JWT Token for authenticated users.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful Login."),
            @ApiResponse(responseCode = "401", description = "Invalid login credentials."),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request. Did not fill in all fields.")})
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody User user) {
        user = userService.validateUser(user);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    @Operation(summary = "Sign up an account.", description = "Create a new account for new users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successful creation of a new account.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400",
                    description = "Empty fields provided or the provided sign up details did not fulfil the requirements"),
            @ApiResponse(responseCode = "409",
                    description = "Email address already exists and the provided email must be unique.")})
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    
    @Operation(summary = "Deposit money to account.",
            description = "Update user balance after depositing the sum of money.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Successfully deposited the specified amount into the user's account.",
            content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User account does not exist.", content = @Content)})
    @PutMapping("/api/deposit")
    public ResponseEntity<Map<String, Boolean>> updateUserBalance(HttpServletRequest request,
            @RequestBody User user) {
        int userId = (Integer) request.getAttribute("userId");
        userService.updateUserBalance(userId, user);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Operation(summary = "Withdraw money from account",
            description = "Update user balance after withdrawing the sum of money.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Successfully withdrew the specified amount from the user's account."),
            @ApiResponse(responseCode = "404", description = "User account does not exist."),
            @ApiResponse(responseCode = "400", description = "Insufficient balance.")})
    @PutMapping("/api/withdraw")
    public ResponseEntity<Map<String, Boolean>> withdrawUserBalance(HttpServletRequest request,
            @RequestBody User user) {
        int userId = (Integer) request.getAttribute("userId");
        user.setBalance(-1.0 * user.getBalance());
        userService.updateUserBalance(userId, user);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    private Map<String, String> generateJWTToken(User user) {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("userId", user.getUserId()).claim("email", user.getEmail()).compact();
        Map<String, String> map = new HashMap<>();
        map.put("stock-market-token", token);
        return map;
    }

}
