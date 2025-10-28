package com.incture.food.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.food.dao.FoodMenuRepository;
import com.incture.food.entities.FoodMenu;

@Service
public class FoodMenuService {
	private static final Logger logger = LoggerFactory.getLogger(FoodMenuService.class);

	@Autowired
	private FoodMenuRepository foodMenuRepository;

	public List<FoodMenu> getAllFood() {
	    logger.info("Fetching all food items");
	    return foodMenuRepository.findAll();
	}

	public FoodMenu addFood(FoodMenu food) {
	    logger.info("Adding new food item: {}", food);
	    if (food.getRestaurant() != null) {
	        logger.debug("Adding food to restaurant with ID: {}", food.getRestaurant().getId());
	        food.getRestaurant().getFoodMenu().add(food);
	    }
	    FoodMenu savedFood = foodMenuRepository.save(food);
	    logger.info("Food item added successfully with ID: {}", savedFood.getId());
	    return savedFood;
	}

	public FoodMenu getFood(int id) {
	    logger.debug("Fetching food item by ID: {}", id);
	    return foodMenuRepository.findById(id)
	            .orElseThrow(() -> {
	                logger.error("FoodMenu not found with ID: {}", id);
	                return new RuntimeException("FoodMenu not found with id: " + id);
	            });
	}

	public FoodMenu updateFood(FoodMenu foodMenu, int id) {
	    logger.info("Updating food item with ID: {}", id);
	    foodMenu.setId(id);
	    if (foodMenu.getRestaurant() != null) {
	        logger.debug("Updating food in restaurant with ID: {}", foodMenu.getRestaurant().getId());
	        foodMenu.getRestaurant().getFoodMenu().add(foodMenu);
	    }
	    FoodMenu updatedFood = foodMenuRepository.save(foodMenu);
	    logger.info("Food item updated successfully with ID: {}", id);
	    return updatedFood;
	}

	public void deleteFood(int id) {
	    logger.info("Deleting food item with ID: {}", id);
	    foodMenuRepository.deleteById(id);
	    logger.info("Food item deleted successfully with ID: {}", id);
	}
	}