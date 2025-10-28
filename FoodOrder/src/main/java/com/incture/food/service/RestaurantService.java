package com.incture.food.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.food.dao.RestaurantRepository;
import com.incture.food.entities.Restaurant;

@Service
public class RestaurantService {
	private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

	@Autowired
	private RestaurantRepository restaurantRepository;

	public List<Restaurant> getAllRestaurants() {
	    logger.info("Fetching all restaurants");
	    return restaurantRepository.findAll();
	}

	public Restaurant addRestaurant(Restaurant restaurant) {
	    logger.info("Adding new restaurant: {}", restaurant);
	    Restaurant savedRestaurant = restaurantRepository.save(restaurant);
	    logger.info("Restaurant added successfully with ID: {}", savedRestaurant.getId());
	    return savedRestaurant;
	}

	public Restaurant getRestaurant(Long id) {
	    logger.debug("Fetching restaurant by ID: {}", id);
	    return restaurantRepository.findById(id)
	            .orElseThrow(() -> {
	                logger.error("Restaurant not found with ID: {}", id);
	                return new RuntimeException("Restaurant not found with id: " + id);
	            });
	}

	public Restaurant updateRestaurant(Restaurant restaurant, Long id) {
	    logger.info("Updating restaurant with ID: {}", id);
	    restaurant.setId(id);
	    Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
	    logger.info("Restaurant updated successfully with ID: {}", id);
	    return updatedRestaurant;
	}

	public void deleteRestaurant(Long id) {
	    logger.info("Deleting restaurant with ID: {}", id);
	    restaurantRepository.deleteById(id);
	    logger.info("Restaurant deleted successfully with ID: {}", id);
	}
	}
