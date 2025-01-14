package com.parkingservice.parking.controllers;

import com.parkingservice.parking.controllers.dto.UserDTO;
import com.parkingservice.parking.controllers.dto.UserFullDTO;
import com.parkingservice.parking.models.User;
import com.parkingservice.parking.services.UserService;
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

    public UserController (UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")

    //

    public ResponseEntity<User> createUser(@RequestBody UserFullDTO userFullDTO) {
        try {
            User createdUser = userService.createUser(userFullDTO);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // User update and deletion methods
    // ...

}