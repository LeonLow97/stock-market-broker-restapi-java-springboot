package com.stockmarket.stockmarketapi.servicetests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.stockmarket.stockmarketapi.DTOs.UserAmountDTO;
import com.stockmarket.stockmarketapi.DTOs.UserLoginDTO;
import com.stockmarket.stockmarketapi.DTOs.UserRegisterDTO;
import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.exception.AuthException;
import com.stockmarket.stockmarketapi.exception.BadRequestException;
import com.stockmarket.stockmarketapi.exception.ResourceAlreadyExistsException;
import com.stockmarket.stockmarketapi.repository.UserRepository;
import com.stockmarket.stockmarketapi.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    private User testUser;
    private User registeredUser;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @BeforeEach
    void setup() throws Exception {
        testUser = new User("leonlow", "Password0!", "leonlow@service.com", 1000.0, 1);
        registeredUser =
                new User("leonlow", "$2a$10$XLR4UYICkiS.rJE6S.7Lhuy92zcRFrqb8wvrvhfNDAHoowiSzWUXu",
                        "leonlow@service.com", 1000.0, 1);
        registeredUser.setIsActive(1);
    }

    @Test
    public void Test_RegisterUserThenReturnUserObject() {
        // Arrange
        UserRegisterDTO userRegisterDTO =
                new UserRegisterDTO("leonlow", "leonlow@service.com", "Password0!");
        when(userRepository.getCountByEmail(userRegisterDTO.getEmail())).thenReturn(0);
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(registeredUser);

        // Act
        UserRegisterDTO returnedUser = userServiceImpl.registerUser(userRegisterDTO);

        // Assert
        assertEquals("leonlow", returnedUser.getUsername());
        assertEquals("Password0!", returnedUser.getPassword());
        assertEquals("leonlow@service.com", returnedUser.getEmail());

        verify(userRepository, times(1)).save(ArgumentMatchers.any(User.class));
        verify(userRepository, times(1)).getCountByEmail(testUser.getEmail());
    }

    @Test
    public void Test_RegisterUserWhenEmailPasswordOrUsernameIsBlank() {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("", "", "");

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.registerUser(userRegisterDTO);
        });

        // Assert
        assertEquals("Fill in all fields.", exception.getMessage());
    }

    @Test
    public void Test_RegisterUserWhenUsernameLengthIsInvalid() {
        // Arrange
        UserRegisterDTO userRegisterDTO =
                new UserRegisterDTO("a", "leonlow@service.com", "Password0!");

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.registerUser(userRegisterDTO);
        });

        // Assert
        assertEquals("Username length must be between 7 - 20 characters.", exception.getMessage());
    }

    @Test
    public void Test_RegisterUserWhenPasswordLengthIsInvalid() {
        // Arrange
        UserRegisterDTO userRegisterDTO =
                new UserRegisterDTO("leonlow", "leonlow@service.com", "Pass");

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.registerUser(userRegisterDTO);
        });

        // Assert
        assertEquals("Password length must be between 10 - 20 characters.", exception.getMessage());
    }

    @Test
    public void Test_RegisterUserWhenEmailLengthIsInvalid() {
        // Arrange
        UserRegisterDTO userRegisterDTO =
                new UserRegisterDTO("leonlow", "test@test.com".repeat(256), "Password0!");

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.registerUser(userRegisterDTO);
        });

        // Assert
        assertEquals("Email Address cannot be more than 255 characters.", exception.getMessage());
    }

    @Test
    public void Test_RegisterUserWhenPasswordFormatIsInvalid() {
        // Arrange
        UserRegisterDTO userRegisterDTO =
                new UserRegisterDTO("leonlow", "leonlow@email.com".repeat(256), "Password");

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.registerUser(userRegisterDTO);
        });

        // Assert
        assertEquals("Password length must be between 10 - 20 characters.", exception.getMessage());
    }

    @Test
    public void Test_RegisterUserWhenEmailFormatIsInvalid() {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("leonlow", "leon", "Password0!");

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.registerUser(userRegisterDTO);
        });

        // Assert
        assertEquals("Invalid email format.", exception.getMessage());
    }

    @Test
    public void Test_RegisterUserWhenEmailIsAlreadyInUse() {
        // Arrange
        User takenEmail = new User("leonlow", "Password0!", "leonlow@email.com", 1000.0, 1);
        UserRegisterDTO userRegisterDTO =
                new UserRegisterDTO("leonlow", "leonlow@email.com", "Password0!");
        when(userRepository.getCountByEmail(takenEmail.getEmail())).thenReturn(1);

        // Act
        Throwable exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            userServiceImpl.registerUser(userRegisterDTO);
        });

        // Assert
        assertEquals("Email already in use.", exception.getMessage());
        verify(userRepository, times(1)).getCountByEmail(takenEmail.getEmail());
    }

    @Test
    public void Test_RegisterUserNullPointerException() {
        // Arrange
        UserRegisterDTO user = null;

        // Act and Assert
        assertThrows(BadRequestException.class, () -> userServiceImpl.registerUser(user));
    }

    @Test
    public void Test_ValidateUserWhenUserIsValid() {
        // Arrange
        UserLoginDTO userLoginDTO = new UserLoginDTO("leonlow@service.com", "Password0!");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User validUser = new User("leonlow", "Password0!", "leonlow@service.com", 1000.0, 1);
        validUser.setIsActive(1);
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(registeredUser);

        // Act
        User dbUser = userServiceImpl.validateUser(userLoginDTO);

        // Assert
        assertEquals(dbUser.getEmail(), validUser.getEmail());
        assertTrue(passwordEncoder.matches(validUser.getPassword(), dbUser.getPassword()));

        verify(userRepository, times(1)).findByEmail(validUser.getEmail());
    }

    @Test
    public void Test_ValidateUserWhenUserConstructorArgsIsIncorrect() {
        // Arrange
        UserLoginDTO userLoginDTO = new UserLoginDTO("Leon Low", "Password0!");
        User invalidUser = new User("leonlow@example.com", "Password0!", "Leon Low", 1000.0, 1);
        when(userRepository.findByEmail(invalidUser.getEmail())).thenReturn(invalidUser);

        // Act
        Throwable exception = assertThrows(AuthException.class, () -> {
            userServiceImpl.validateUser(userLoginDTO);
        });

        // Assert
        assertEquals("Incorrect email/password. Please try again.", exception.getMessage());
    }

    @Test
    public void Test_ValidateUserWhenEmailIsBlank() {
        // Arrange
        UserLoginDTO userLoginDTO = new UserLoginDTO("", "Password0!");

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.validateUser(userLoginDTO);
        });

        // Assert
        assertEquals("Fill in all fields.", exception.getMessage());
    }

    @Test
    public void Test_ValidateUserWhenPasswordIsBlank() {
        // Arrange
        UserLoginDTO userLoginDTO = new UserLoginDTO("leonlow@email.com", "");

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.validateUser(userLoginDTO);
        });

        // Assert
        assertEquals("Fill in all fields.", exception.getMessage());
    }

    @Test
    public void Test_ValidateUserWhenInvalidPasswordLength() {
        // Arrange
        UserLoginDTO userLoginDTO =
                new UserLoginDTO("leonlow@email.com", "Password19438534345345!!!!@#234");

        // Act
        Throwable exception = assertThrows(AuthException.class, () -> {
            userServiceImpl.validateUser(userLoginDTO);
        });

        // Assert
        assertEquals("Incorrect email/password. Please try again.", exception.getMessage());
    }

    @Test
    public void Test_ValidateUserWhenInvalidEmailLength() {
        // Arrange
        UserLoginDTO userLoginDTO = new UserLoginDTO("test@test.com", "Password0!");

        // Act
        Throwable exception = assertThrows(AuthException.class, () -> {
            userServiceImpl.validateUser(userLoginDTO);
        });

        // Assert
        assertEquals("Incorrect email/password. Please try again.", exception.getMessage());
    }

    @Test
    public void Test_ValidateUserWhenUserIsInactive() {
        // Arrange
        UserLoginDTO userLoginDTO = new UserLoginDTO("leonlow@service.com", "Password0!");
        User inactiveUser = new User("leonlow", "Password0!", "leonlow@service.com", 1000.0, 1);
        User inactiveDbUser =
                new User("leonlow", "$2a$10$XLR4UYICkiS.rJE6S.7Lhuy92zcRFrqb8wvrvhfNDAHoowiSzWUXu",
                        "leonlow@service.com", 1000.0, 1);
        inactiveDbUser.setIsActive(0);
        when(userRepository.findByEmail(inactiveUser.getEmail())).thenReturn(inactiveDbUser);


        // Act
        Throwable exception = assertThrows(AuthException.class, () -> {
            userServiceImpl.validateUser(userLoginDTO);
        });

        // Assert
        assertEquals("This account has been disabled.", exception.getMessage());
    }

    @Test
    public void Test_ValidateUserNullPointerException() {
        // Arrange
        UserLoginDTO userLoginDuo = null;

        // Act and Assert
        assertThrows(AuthException.class, () -> userServiceImpl.validateUser(userLoginDuo));
    }

    @Test
    public void Test_UpdateUserBalanceWhenUserIsValid() {
        // Arrange
        UserAmountDTO userAmount = new UserAmountDTO();
        userAmount.setAmount(1500.0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(registeredUser));
        when(userRepository.save(registeredUser)).thenReturn(registeredUser);

        // Act
        userServiceImpl.updateUserBalance(1, userAmount);

        // Assert
        assertEquals(registeredUser.getBalance(), 2500.0);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(registeredUser);
    }

    @Test
    public void Test_UpdateUserBalanceWithInsufficientBalance() {
        // Arrange
        UserAmountDTO userAmount = new UserAmountDTO();
        userAmount.setAmount(-3000.0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(registeredUser));

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.updateUserBalance(1, userAmount);
        });

        assertEquals("Balance is insufficient for the specified withdrawal amount.",
                exception.getMessage());
    }
}
