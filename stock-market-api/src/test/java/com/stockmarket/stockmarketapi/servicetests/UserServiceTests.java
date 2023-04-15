package com.stockmarket.stockmarketapi.servicetests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
        testUser = new User("leonlow", "Password0!", "leonlow@service.com", 1000.0);
        registeredUser = new User("leonlow", "$2a$10$XLR4UYICkiS.rJE6S.7Lhuy92zcRFrqb8wvrvhfNDAHoowiSzWUXu",
                "leonlow@service.com", 1000.0);
        registeredUser.setIsActive(1);
    }

    @Test
    public void Test_RegisterUserThenReturnUserObject() {
        // Arrange
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userRepository.getCountByEmail(testUser.getEmail())).thenReturn(0);

        // Act
        registeredUser = userServiceImpl.registerUser(testUser);

        // Assert
        assertEquals("leonlow", registeredUser.getUsername());
        assertTrue(passwordEncoder.matches("Password0!", registeredUser.getPassword()));
        assertEquals("leonlow@service.com", registeredUser.getEmail());
        assertEquals(1000.0, registeredUser.getBalance());

        verify(userRepository, times(1)).save(testUser);
        verify(userRepository, times(1)).getCountByEmail(testUser.getEmail());
    }

    @Test
    public void Test_RegisterUserWhenEmailPasswordOrUsernameIsBlank() {
        // Arrange
        User blankUser = new User("", "", "", 0.0);

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.registerUser(blankUser);
        });

        // Assert
        assertEquals("Fill in all fields.", exception.getMessage());
    }

    @Test
    public void Test_RegisterUserWhenUsernameLengthIsInvalid() {
        // Arrange
        User invalidUsername = new User("a", "Password0!", "leonlow@service.com", 1000.0);

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.registerUser(invalidUsername);
        });

        // Assert
        assertEquals("Username length must be between 7 - 20 characters.", exception.getMessage());
    }

    @Test
    public void Test_RegisterUserWhenPasswordLengthIsInvalid() {
        // Arrange
        User invalidPassword = new User("leonlow", "Pass", "leonlow@service.com", 1000.0);

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.registerUser(invalidPassword);
        });

        // Assert
        assertEquals("Password length must be between 10 - 20 characters.", exception.getMessage());
    }

    @Test
    public void Test_RegisterUserWhenEmailLengthIsInvalid() {
        // Arrange
        User invalidEmail = new User("leonlow", "Password0!",
                "test@test.com".repeat(256),
                1000.0);

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.registerUser(invalidEmail);
        });

        // Assert
        assertEquals("Email Address cannot be more than 255 characters.", exception.getMessage());
    }

    @Test
    public void Test_RegisterUserWhenPasswordFormatIsInvalid() {
        // Arrange
        User invalidPassword = new User("leonlow", "Password",
                "leonlow",
                1000.0);

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.registerUser(invalidPassword);
        });

        // Assert
        assertEquals("Password length must be between 10 - 20 characters.", exception.getMessage());
    }

    @Test
    public void Test_RegisterUserWhenEmailFormatIsInvalid() {
        // Arrange
        User invalidEmail = new User("leonlow", "Password0!",
                "leonlow",
                1000.0);

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.registerUser(invalidEmail);
        });

        // Assert
        assertEquals("Invalid email format.", exception.getMessage());
    }

    @Test
    public void Test_RegisterUserWhenEmailIsAlreadyInUse() {
        // Arrange
        User takenEmail = new User("leonlow", "Password0!", "leonlow@email.com", 1000.0);
        when(userRepository.getCountByEmail(takenEmail.getEmail())).thenReturn(1);

        // Act
        Throwable exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            userServiceImpl.registerUser(takenEmail);
        });

        // Assert
        assertEquals("Email already in use.", exception.getMessage());
        verify(userRepository, times(1)).getCountByEmail(takenEmail.getEmail());
    }

    @Test
    public void Test_RegisterUserNullPointerException() {
        // Arrange
        User user = null;

        // Act and Assert
        assertThrows(BadRequestException.class, () -> userServiceImpl.registerUser(user));
    }

    @Test
    public void Test_ValidateUserWhenUserIsValid() {
        // Arrange
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User validUser = new User("leonlow", "Password0!", "leonlow@service.com", 1000.0);
        validUser.setIsActive(1);
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(registeredUser);

        // Act
        User dbUser = userServiceImpl.validateUser(validUser);

        // Assert
        assertEquals(dbUser.getEmail(), validUser.getEmail());
        assertTrue(passwordEncoder.matches(validUser.getPassword(), dbUser.getPassword()));

        verify(userRepository, times(1)).findByEmail(validUser.getEmail());
    }

    @Test
    public void Test_ValidateUserWhenUserIsInvalid() {
        // Arrange
        User invalidUser = new User("leonlow@example.com", "Password55#!@", "Leon Low", 1000.0);
        when(userRepository.findByEmail(invalidUser.getEmail())).thenReturn(invalidUser);

        // Act
        Throwable exception = assertThrows(AuthException.class, () -> {
            userServiceImpl.validateUser(invalidUser);
        });

        // Assert
        assertEquals("Incorrect email/password. Please try again.", exception.getMessage());
    }

    @Test
    public void Test_ValidateUserWhenEmailIsBlank() {
        // Arrange
        User blankEmail = new User("leonlow", "Password0!", "", 1000.0);

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.validateUser(blankEmail);
        });

        // Assert
        assertEquals("Fill in all fields.", exception.getMessage());
    }

    @Test
    public void Test_ValidateUserWhenPasswordIsBlank() {
        // Arrange
        User blankPassword = new User("leonlow", "", "leonlow@email.com", 1000.0);

        // Act
        Throwable exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.validateUser(blankPassword);
        });

        // Assert
        assertEquals("Fill in all fields.", exception.getMessage());
    }

    @Test
    public void Test_ValidateUserWhenInvalidPasswordLength() {
        // Arrange
        User invalidPassword = new User("leonlow", "Password19438534345345!!!!@#234", "leonlow@email.com", 1000.0);

        // Act
        Throwable exception = assertThrows(AuthException.class, () -> {
            userServiceImpl.validateUser(invalidPassword);
        });

        // Assert
        assertEquals("Incorrect email/password. Please try again.", exception.getMessage());
    }

    @Test
    public void Test_ValidateUserWhenInvalidEmailLength() {
        // Arrange
        User invalidEmail = new User("leonlow", "Password0!", "test@test.com".repeat(256), 1000.0);

        // Act
        Throwable exception = assertThrows(AuthException.class, () -> {
            userServiceImpl.validateUser(invalidEmail);
        });

        // Assert
        assertEquals("Incorrect email/password. Please try again.", exception.getMessage());
    }

    @Test
    public void Test_ValidateUserWhenUserIsInactive() {
        // Arrange
        User inactiveUser = new User("leonlow", "Password0!", "leonlow@service.com", 1000.0);
        User inactiveDbUser = new User("leonlow", "$2a$10$XLR4UYICkiS.rJE6S.7Lhuy92zcRFrqb8wvrvhfNDAHoowiSzWUXu",
        "leonlow@service.com", 1000.0);
        inactiveDbUser.setIsActive(0);
        when(userRepository.findByEmail(inactiveUser.getEmail())).thenReturn(inactiveDbUser);

        // Act
        Throwable exception = assertThrows(AuthException.class, () -> {
            userServiceImpl.validateUser(inactiveUser);
        });

        // Assert
        assertEquals("This account has been disabled.", exception.getMessage());
    }

    @Test
    public void Test_ValidateUserNullPointerException() {
        // Arrange
        User user = null;

        // Act and Assert
        assertThrows(AuthException.class, () -> userServiceImpl.validateUser(user));
    }
}
