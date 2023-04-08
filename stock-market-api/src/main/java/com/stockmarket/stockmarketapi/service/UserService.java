package com.stockmarket.stockmarketapi.service;

import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.exception.AuthException;
import com.stockmarket.stockmarketapi.exception.BadRequestException;

public interface UserService {
  
  User validateUser(User user) throws BadRequestException, AuthException;
  User registerUser(User user) throws AuthException;

}
