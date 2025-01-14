package com.parkingservice.parking.controllers;

import com.parkingservice.parking.controllers.dto.UserDTO;
import com.parkingservice.parking.controllers.dto.UserFullDTO;
import com.parkingservice.parking.models.User;
import com.parkingservice.parking.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;
    private final static Logger logger = LogManager.getLogger(UserController.class);

    public UserController (UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")

    //

    public ResponseEntity<User> createUser(@RequestBody UserFullDTO userFullDTO) {
        logger.info("Creating user at POST request for /api/users/register.");
        try {
            User createdUser = userService.createUser(userFullDTO);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.warn("User creation failed. User data incorrect or incomplete: " + userFullDTO);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Handling GET request for /api/users.");
        try {
            List<User> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("No users found at GET request.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("Handling GET request for /api/users/" + id);
        try {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("User not found with ID " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // User update and deletion methods
    // ...

}