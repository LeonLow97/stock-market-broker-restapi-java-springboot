package com.stockmarket.stockmarketapi.service;

import com.stockmarket.stockmarketapi.entity.User;

public interface UserService {

  User validateUser(User user);

  User registerUser(User user);

  void updateUserBalance(Integer userId, User user);

}
