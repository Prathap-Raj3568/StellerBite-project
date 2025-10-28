package com.incture.food.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.food.entities.User;
import com.incture.food.service.UserService;

@RestController
@RequestMapping("food_api")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody User user) {
	    logger.info("POST /register - Registering new user with email: {}", user.getEmail());
	    try {
	        userService.saveUser(user);
	        logger.info("User registered successfully with email: {}", user.getEmail());
	        return ResponseEntity.ok("User registered successfully");
	    } catch (Exception e) {
	        logger.error("Registration failed for email: {} - {}", user.getEmail(), e.getMessage());
	        return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
	    }
	}

	@GetMapping("/user")
	public ResponseEntity<List<User>> getAllUsers() {
	    logger.info("GET /user - Fetching all users");
	    return ResponseEntity.ok(userService.getAllUser());
	}

	@GetMapping("/user/{user_id}")
	public ResponseEntity<User> getUser(@PathVariable("user_id") Long id) {
	    logger.info("GET /user/{} - Fetching user", id);
	    return ResponseEntity.ok(userService.getUser(id));
	}

	@PostMapping("/user")
	public ResponseEntity<User> addUser(@RequestBody User user) {
	    logger.info("POST /user - Adding new user");
	    user.setPassword(passwordEncoder.encode(user.getPassword()));
	    return ResponseEntity.ok(userService.AddUser(user));
	}

	@PutMapping("/user/{user_id}")
	public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("user_id") Long id) {
	    logger.info("PUT /user/{} - Updating user", id);
	    return ResponseEntity.ok(userService.updateUser(user, id));
	}

	@DeleteMapping("/user/{user_id}")
	public ResponseEntity<Void> deleteUser(@PathVariable("user_id") Long id) {
	    logger.info("DELETE /user/{} - Deleting user", id);
	    userService.deleteUser(id);
	    logger.info("User deleted successfully with ID: {}", id);
	    return ResponseEntity.noContent().build();
	}
	}