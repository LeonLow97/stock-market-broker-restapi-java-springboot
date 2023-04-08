package com.stockmarket.stockmarketapi.service;

import java.util.regex.Pattern;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.exception.AuthException;
import com.stockmarket.stockmarketapi.exception.BadRequestException;
import com.stockmarket.stockmarketapi.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepository userRepository;

  @Override
  public User validateUser(User user) throws BadRequestException, AuthException {
    try {
      if (user.getEmail() == null || user.getEmail().isBlank() || user.getPassword() == null
          || user.getPassword().isBlank()) {
        throw new BadRequestException("Fill in all fields.");
      }
      if (user.getPassword().length() < 10 || user.getPassword().length() > 20
          || user.getEmail().length() > 255) {
        throw new AuthException("Incorrect email/password. Please try again.");
      }

      // Validate User
      User dbUser = userRepository.findByEmail(user.getEmail());
      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
      if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
        throw new AuthException("Incorrect email/password. Please try again.");
      }
    } catch (NullPointerException e) {
      System.out.println("validateUser: " + e.getMessage());
      throw new AuthException("Incorrect email/password. Please try again.");
    }

    return user;

  }

  @Override
  public User registerUser(User user) {
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


    user.setEmail(user.getEmail().toLowerCase());
    if (!emailFormatCheck(user.getEmail()))
      throw new BadRequestException("Invalid email format");

    if (!passwordFormatCheck(user.getPassword()))
      throw new BadRequestException(
          "Invalid password format. Password must contain at least one lowercase and uppercase character, number, and special character.");

    Integer emailCount = userRepository.getCountByEmail(user.getEmail());
    if (emailCount >= 1)
      throw new BadRequestException("Email already in use.");

    user.setIsActive(1);

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepository.save(user);
  }

  private Boolean emailFormatCheck(String email) {
    Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");
    return emailPattern.matcher(email).matches();
  }

  private Boolean passwordFormatCheck(String password) {
    Pattern passwordPattern = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+{\\[\\]}\\\\|;:'\",.<>/?`~]).+$");
    return passwordPattern.matcher(password).matches();
  }

}
