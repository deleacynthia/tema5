package com.example.tema5.service;

import com.example.tema5.model.User;
import com.example.tema5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("User with the same email already exists");
        }
        return userRepository.save(user);
    }


    public boolean isPasswordCorrect(User user, String password) {
        return user.getPassword().equals(password);
    }


}
