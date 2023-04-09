package com.stockmarket.stockmarketapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFilledException extends RuntimeException {

  public OrderNotFilledException(String message) {
    super(message);
  }
  
}
