package com.incture.food.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.food.entities.Restaurant;
import com.incture.food.service.RestaurantService;

@RestController
@RequestMapping("food_api")
public class RestaurantController {
	private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

	@Autowired
	private RestaurantService restaurantService;

	@GetMapping("/restaurant")
	public ResponseEntity<List<Restaurant>> getAllRestaurants() {
	    logger.info("GET /restaurant - Fetching all restaurants");
	    return ResponseEntity.ok(restaurantService.getAllRestaurants());
	}

	@GetMapping("/restaurant/{resturant_id}")
	public ResponseEntity<Restaurant> getRestaurant(@PathVariable("resturant_id") Long id) {
	    logger.info("GET /restaurant/{} - Fetching restaurant", id);
	    return ResponseEntity.ok(restaurantService.getRestaurant(id));
	}

	@PostMapping("/restaurant")
	public ResponseEntity<Restaurant> addRestaurant(@RequestBody Restaurant restaurant) {
	    logger.info("POST /restaurant - Adding new restaurant");
	    return ResponseEntity.ok(restaurantService.addRestaurant(restaurant));
	}

	@PutMapping("/restaurant/{resturant_id}")
	public ResponseEntity<Restaurant> updateRestaurant(@RequestBody Restaurant restaurant, @PathVariable("resturant_id") Long id) {
	    logger.info("PUT /restaurant/{} - Updating restaurant", id);
	    return ResponseEntity.ok(restaurantService.updateRestaurant(restaurant, id));
	}

	@DeleteMapping("/restaurant/{resturant_id}")
	public ResponseEntity<Void> deleteRestaurant(@PathVariable("resturant_id") Long id) {
	    logger.info("DELETE /restaurant/{} - Deleting restaurant", id);
	    restaurantService.deleteRestaurant(id);
	    logger.info("Restaurant deleted successfully with ID: {}", id);
	    return ResponseEntity.noContent().build();
	}
	}