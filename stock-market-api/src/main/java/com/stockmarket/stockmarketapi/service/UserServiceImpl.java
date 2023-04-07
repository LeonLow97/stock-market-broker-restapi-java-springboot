package com.stockmarket.stockmarketapi.service;

import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.exception.BadRequestException;
import com.stockmarket.stockmarketapi.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepository userRepository;

  @Override
  public User addUser(User user) {
    // Ensure email and password are filled
    if (user.getEmail() == null || user.getEmail().isBlank() || user.getPassword() == null
        || user.getPassword().isBlank() || user.getUsername() == null
        || user.getUsername().isBlank()) {
      throw new BadRequestException("Fill in all fields.");
    }

    // Check Length of username, password and email
    if (user.getUsername().length() < 7 || user.getUsername().length() > 20)
      throw new BadRequestException("Username length must be between 7 - 20 characters.");
    if (user.getPassword().length() < 10 || user.getPassword().length() > 20)
      throw new BadRequestException("Password length must be between 10 - 20 characters.");
    if (user.getEmail().length() > 255)
      throw new BadRequestException("Email Address cannot be more than 255 characters.");


    Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");
    user.setEmail(user.getEmail().toLowerCase());
    if (!emailPattern.matcher(user.getEmail()).matches())
      throw new BadRequestException("Invalid email format");

    Pattern passwordPattern = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+{\\[\\]}\\\\|;:'\",.<>/?`~]).+$");
    if (!passwordPattern.matcher(user.getPassword()).matches())
      throw new BadRequestException(
          "Invalid password format. Password must contain at least one lowercase and uppercase character, number, and special character.");

    user.setIsActive(1);

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepository.save(user);
  }

}
