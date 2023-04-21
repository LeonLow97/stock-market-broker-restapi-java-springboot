package com.stockmarket.stockmarketapi.service;

import java.util.Optional;
import java.util.regex.Pattern;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.stockmarket.stockmarketapi.DTOs.UserAmountDTO;
import com.stockmarket.stockmarketapi.DTOs.UserLoginDTO;
import com.stockmarket.stockmarketapi.DTOs.UserRegisterDTO;
import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.exception.AuthException;
import com.stockmarket.stockmarketapi.exception.BadRequestException;
import com.stockmarket.stockmarketapi.exception.ResourceAlreadyExistsException;
import com.stockmarket.stockmarketapi.exception.ResourceNotFoundException;
import com.stockmarket.stockmarketapi.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepository userRepository;

  @Override
  public UserRegisterDTO registerUser(UserRegisterDTO userRegisterDTO) {
    try {
      // Ensure email and password are filled
      if (userRegisterDTO.getEmail().isBlank() || userRegisterDTO.getPassword().isBlank()
          || userRegisterDTO.getUsername().isBlank()) {
        throw new BadRequestException("Fill in all fields.");
      }

      // Check Length of username, password and email
      if (userRegisterDTO.getUsername().length() < 7 || userRegisterDTO.getUsername().length() > 20)
        throw new BadRequestException("Username length must be between 7 - 20 characters.");
      if (userRegisterDTO.getPassword().length() < 10
          || userRegisterDTO.getPassword().length() > 20)
        throw new BadRequestException("Password length must be between 10 - 20 characters.");
      if (userRegisterDTO.getEmail().length() > 255)
        throw new BadRequestException("Email Address cannot be more than 255 characters.");

      userRegisterDTO.setEmail(userRegisterDTO.getEmail().toLowerCase());
      if (!emailFormatCheck(userRegisterDTO.getEmail()))
        throw new BadRequestException("Invalid email format.");

      if (!passwordFormatCheck(userRegisterDTO.getPassword()))
        throw new BadRequestException(
            "Invalid password format. Password must contain at least one lowercase and uppercase character, number, and special character.");

      Integer emailCount = userRepository.getCountByEmail(userRegisterDTO.getEmail());
      if (emailCount >= 1)
        throw new ResourceAlreadyExistsException("Email already in use.");

      User insertUser = new User(userRegisterDTO.getUsername(), userRegisterDTO.getPassword(),
          userRegisterDTO.getEmail(), 1000.0, 1);

      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
      insertUser.setPassword(passwordEncoder.encode(insertUser.getPassword()));
      userRepository.save(insertUser);

      return userRegisterDTO;
    } catch (NullPointerException e) {
      throw new BadRequestException(e.getMessage());
    }
  }

  @Override
  public User validateUser(UserLoginDTO userLoginDTO) {
    try {
      if (userLoginDTO.getEmail() == null || userLoginDTO.getEmail().isBlank()
          || userLoginDTO.getPassword() == null || userLoginDTO.getPassword().isBlank()) {
        throw new BadRequestException("Fill in all fields.");
      }
      if (userLoginDTO.getPassword().length() < 10 || userLoginDTO.getPassword().length() > 20
          || userLoginDTO.getEmail().length() > 255) {
        throw new AuthException("Incorrect email/password. Please try again.");
      }

      // Validate User
      User dbUser = userRepository.findByEmail(userLoginDTO.getEmail());
      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
      if (!passwordEncoder.matches(userLoginDTO.getPassword(), dbUser.getPassword())) {
        throw new AuthException("Incorrect email/password. Please try again.");
      }

      // Check if account is disabled
      if (dbUser.getIsActive() != 1) {
        throw new AuthException("This account has been disabled.");
      }

      return dbUser;
    } catch (NullPointerException e) {
      throw new AuthException("Incorrect email/password. Please try again.");
    }
  }

  @Override
  public UserAmountDTO updateUserBalance(int userId, UserAmountDTO userAmountDTO) {
    Optional<User> dbUser = userRepository.findById(Long.valueOf(userId));
    double dbBalance = dbUser.map(u -> u.getBalance())
        .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    double updatedBalance = dbBalance + userAmountDTO.getAmount();

    // Prevent user from withdrawing beyond $0
    if (updatedBalance < 0.0) {
      throw new BadRequestException("Balance is insufficient for the specified withdrawal amount.");
    }

    User updatedUser = dbUser.get();
    updatedUser.setBalance(updatedBalance);
    userRepository.save(updatedUser);

    userAmountDTO.setBalance(updatedBalance);

    return userAmountDTO;
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
