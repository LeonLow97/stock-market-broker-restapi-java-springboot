package com.stockmarket.stockmarketapi.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/login")
    public ResponseEntity<String> Login() {
        String response = "You have successfully logged in!";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> Logout() {
        String response = "You have successfully logged out!";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/signup")
    public ResponseEntity<String> SignUp() {
        String response = "You have successfully signed up!";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
