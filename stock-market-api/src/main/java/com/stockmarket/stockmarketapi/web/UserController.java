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
import com.stockmarket.stockmarketapi.DTOs.UserLoginDTO;
import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User Controller", description = "To sign up for an account and log in with the provided login credentials. Provides endpoint for withdrawing and depositing cash into the created account.")
public class UserController {

        @Autowired
        UserService userService;

        @PostMapping("/register")
        public ResponseEntity<User> registerUser(@RequestBody User user) {
                userService.registerUser(user);
                return new ResponseEntity<>(user, HttpStatus.CREATED);
        }

        @PostMapping("/login")
        public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
                User user = userService.validateUser(userLoginDTO);
                return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
        }

        @PutMapping("/api/deposit")
        public ResponseEntity<Map<String, Boolean>> depositUserBalance(HttpServletRequest request,
                        @RequestBody User user) {
                Integer userId = (Integer) request.getAttribute("userId");
                userService.updateUserBalance(userId, user);
                Map<String, Boolean> map = new HashMap<>();
                map.put("success", true);
                return new ResponseEntity<>(map, HttpStatus.OK);
        }

        @PutMapping("/api/withdraw")
        public ResponseEntity<Map<String, Boolean>> withdrawUserBalance(HttpServletRequest request,
                        @RequestBody User user) {
                Integer userId = (Integer) request.getAttribute("userId");
                user.setBalance(-1.0 * user.getBalance());
                userService.updateUserBalance(userId, user);
                Map<String, Boolean> map = new HashMap<>();
                map.put("success", true);
                return new ResponseEntity<>(map, HttpStatus.OK);
        }

        public static Map<String, String> generateJWTToken(User user) {
                long timestamp = System.currentTimeMillis();
                String token = Jwts.builder()
                                .signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                                .setIssuedAt(new Date(timestamp))
                                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                                .claim("userId", user.getUserId()).claim("email", user.getEmail())
                                .compact();
                Map<String, String> map = new HashMap<>();
                map.put("stock-market-token", token);
                return map;
        }

}
