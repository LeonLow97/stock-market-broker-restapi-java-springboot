package com.stockmarket.stockmarketapi.service;

import com.stockmarket.stockmarketapi.DTOs.UserAmountDTO;
import com.stockmarket.stockmarketapi.DTOs.UserLoginDTO;
import com.stockmarket.stockmarketapi.DTOs.UserRegisterDTO;
import com.stockmarket.stockmarketapi.entity.User;

public interface UserService {

  User validateUser(UserLoginDTO userLoginDTO);

  UserRegisterDTO registerUser(UserRegisterDTO userRegisterDTO);

  UserAmountDTO updateUserBalance(int userId, UserAmountDTO userAmount);

}
