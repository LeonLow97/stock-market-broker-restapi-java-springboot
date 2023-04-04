package com.stockmarket.stockmarketapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepository userRepository;

  @Override
  public User addUser(User user) {
    return userRepository.save(user);
  }

}
