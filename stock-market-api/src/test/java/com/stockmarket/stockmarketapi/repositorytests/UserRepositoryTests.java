package com.stockmarket.stockmarketapi.repositorytests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.stockmarket.stockmarketapi.entity.User;
import com.stockmarket.stockmarketapi.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {
        user = new User("leonlow", "Password0!", "leonlow@service.com", 1000.0, 1);
    }

    @Test
    void InjectedComponentsAreNotNull() {
        assertNotNull(userRepository);
    }

    @Test
    public void Test_GetCountByEmailSuccess() {
        // Arrange
        userRepository.save(user);

        // Act
        int count = userRepository.getCountByEmail(user.getEmail());

        // Assert
        assertEquals(1, count);

        // Cleanup
        userRepository.delete(user);
    }

    @Test
    public void Test_GetCountByEmailWhenEmailDoesNotExist() {
        // Arrange
        userRepository.save(user);

        // Act
        int count = userRepository.getCountByEmail("notatrueemail@fake.com");

        // Assert
        assertEquals(0, count);

        // Cleanup
        userRepository.delete(user);
    }

    @Test
    public void Test_FindByEmailSuccess() {
        // Arrange
        User dbUser = userRepository.save(user);

        // Act
        User findUser = userRepository.findByEmail(dbUser.getEmail());

        // Assert
        assertEquals(dbUser, findUser);
        assertEquals(dbUser.getEmail(), findUser.getEmail());
        assertEquals(dbUser.getUsername(), findUser.getUsername());

        // Cleanup
        userRepository.delete(user);
    }

    @Test
    public void Test_FindByEmailWhenEmailDoesNotExist() {
        // Arrange
        User anotherUser = new User("leonlow", "Password0!",
                "emailnotthesame@test.com", 1000.0, 1);
        userRepository.save(user);

        // Act
        User findUser = userRepository.findByEmail(anotherUser.getEmail());

        // Assert
        assertNull(findUser);

        // Cleanup
        userRepository.delete(user);
    }

}
