package com.stockmarket.stockmarketapi.response;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WriteJSON {
  private String message;
  private int code;
  private Map<String, String> errors;

  public WriteJSON(String message, int code) {
    this.message = message;
    this.code = code;
  }

  public WriteJSON(String message, int code, Map<String, String> errors) {
    this.message = message;
    this.code = code;
    this.errors = errors;
  }
}
