package com.incture.food.service;

import java.util.Collection;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.incture.food.dao.UserRepository;
import com.incture.food.entities.User;

@Service
public class UserServiceImpl implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    logger.debug("Loading user by username/email: {}", username);
	    User user = userRepository.findByEmail(username);
	    if (user == null) {
	        logger.error("User not found for email: {}", username);
	        throw new UsernameNotFoundException("User not found for email: " + username);
	    }
	    Collection<SimpleGrantedAuthority> authorities = user.getRoles().stream()
	            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
	            .collect(Collectors.toList());
	    logger.debug("User found with email: {}, roles: {}", username, authorities);
	    return new org.springframework.security.core.userdetails.User(
	            user.getEmail(), user.getPassword(), authorities);
	}
	}