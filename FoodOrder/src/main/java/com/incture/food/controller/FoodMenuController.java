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

import com.incture.food.entities.FoodMenu;
import com.incture.food.service.FoodMenuService;

@RestController
@RequestMapping("food_api")
public class FoodMenuController {
	private static final Logger logger = LoggerFactory.getLogger(FoodMenuController.class);

	@Autowired
	private FoodMenuService foodMenuService;

	@GetMapping("/food")
	public ResponseEntity<List<FoodMenu>> getAllFood() {
	    logger.info("GET /food - Fetching all food items");
	    return ResponseEntity.ok(foodMenuService.getAllFood());
	}

	@GetMapping("/food/{food_id}")
	public ResponseEntity<FoodMenu> getFood(@PathVariable("food_id") int id) {
	    logger.info("GET /food/{} - Fetching food item", id);
	    return ResponseEntity.ok(foodMenuService.getFood(id));
	}

	@PostMapping("/food")
	public ResponseEntity<FoodMenu> addFood(@RequestBody FoodMenu food) {
	    logger.info("POST /food - Adding new food item");
	    return ResponseEntity.ok(foodMenuService.addFood(food));
	}

	@PutMapping("/food/{food_id}")
	public ResponseEntity<FoodMenu> updateFood(@RequestBody FoodMenu food, @PathVariable("food_id") int id) {
	    logger.info("PUT /food/{} - Updating food item", id);
	    return ResponseEntity.ok(foodMenuService.updateFood(food, id));
	}

	@DeleteMapping("/food/{food_id}")
	public ResponseEntity<Void> deleteFood(@PathVariable("food_id") int id) {
	    logger.info("DELETE /food/{} - Deleting food item", id);
	    foodMenuService.deleteFood(id);
	    logger.info("Food item deleted successfully with ID: {}", id);
	    return ResponseEntity.noContent().build();
	}
	}