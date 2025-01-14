package com.parkingservice.parking.services;

import com.parkingservice.parking.controllers.dto.UserDTO;
import com.parkingservice.parking.controllers.dto.UserFullDTO;
import com.parkingservice.parking.models.User;
import com.parkingservice.parking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserFullDTO userFullDTO) {
        // Uniqueness check
        if (userRepository.existsByUsername(userFullDTO.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }
        if (userRepository.existsByEmail(userFullDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already taken!");
        }

        // Password is stored as plain text

        User user = new User();
        user.setUsername(userFullDTO.getUsername());
        user.setRole(userFullDTO.getRole());
        user.setEmail(userFullDTO.getEmail());
        user.setPassword(userFullDTO.getPassword());
        user.setPhoneNumber(userFullDTO.getPhoneNumber());
        user.setFullName(userFullDTO.getFullName());

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    // ... User update and deletion methods
}