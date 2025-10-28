package com.incture.food.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.incture.food.dao.UserRepository;
import com.incture.food.entities.User;

@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void saveUser(User user) {
	    logger.info("Saving user with email: {}", user.getEmail());
	    user.setPassword(passwordEncoder.encode(user.getPassword()));
	    userRepository.save(user);
	    logger.info("User saved successfully with email: {}", user.getEmail());
	}

	public List<User> getAllUser(){
	    logger.info("Fetching all users");
	    Iterable<User> iterable=userRepository.findAll();
	    List<User> list=(List<User>)iterable;
	    logger.debug("Found {} users", list.size());
	    return list;
	}

	public User AddUser(User us) {
	    logger.info("Adding new user: {}", us);
	    User u=userRepository.save(us);
	    logger.info("User added successfully with ID: {}", u.getId());
	    return u;
	}

	public User findUser(String email) {
	    logger.debug("Finding user by email: {}", email);
	    return userRepository.findByEmail(email);
	}

	public User getUser(Long id) {
	    logger.debug("Fetching user by ID: {}", id);
	    User user=null;
	    Optional<User> optional=userRepository.findById(id);
	    if(optional.isPresent()) {
	        user=optional.get();
	        logger.debug("User found with ID: {}", id);
	    } else {
	        logger.error("User not found with ID: {}", id);
	    }
	    return user;
	}

	public User updateUser(User user,Long id) {
	    logger.info("Updating user with ID: {}", id);
	    user.setId(id);
	    User updatedUser = userRepository.save(user);
	    logger.info("User updated successfully with ID: {}", id);
	    return updatedUser;
	}

	public void deleteUser(Long id) {
	    logger.info("Deleting user with ID: {}", id);
	    userRepository.deleteById(id);
	    logger.info("User deleted successfully with ID: {}", id);
	}
	}