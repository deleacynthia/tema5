package com.example.tema5.service;

import com.example.tema5.model.User;
import com.example.tema5.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void testCreateUser() {
        User user = new User(2L, "test", "test@gmail.com", "1234");
        User createdUser = userService.createUser(user);
        assertEquals("test@gmail.com", createdUser.getEmail());
    }


    @Test
    public void testCreateUserWithInvalidEmail() {
        User user = new User(null, "test", "testgmail.com", "1234");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }


    @Test
    public void testCreateUserWithDuplicateEmail() {
        User user1 = new User(null, "test2", "test2@gmail.com", "1234");
        User user2 = new User(null, "test3", "test2@gmail.com", "1234");

        userService.createUser(user1);

        assertThrows(IllegalStateException.class, () -> userService.createUser(user2));
    }


    @Test
    public void testFindUserById() {
        User user = new User(1L, "test1", "test1@gmail.com", "1234");
        User createdUser = userService.createUser(user);
        assertTimeout(ofSeconds(1), () -> {
            userService.findUserById(createdUser.getId());
        });
    }

    @Test
    public void testPassword() {
        User user = new User(null, "test", "test@gmail.com", "1234");
        boolean isPasswordCorrect = userService.isPasswordCorrect(user, "1234");
        assertTrue(isPasswordCorrect);
    }

}
