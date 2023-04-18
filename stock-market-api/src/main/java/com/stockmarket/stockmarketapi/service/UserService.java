package com.stockmarket.stockmarketapi.service;

import com.stockmarket.stockmarketapi.DTOs.UserLoginDTO;
import com.stockmarket.stockmarketapi.entity.User;

public interface UserService {

  User validateUser(UserLoginDTO userLoginDTO);

  User registerUser(User user);

  void updateUserBalance(Integer userId, User user);

}
