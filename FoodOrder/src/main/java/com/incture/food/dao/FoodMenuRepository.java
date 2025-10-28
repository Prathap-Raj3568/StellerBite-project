package com.incture.food.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incture.food.entities.FoodMenu;
import com.incture.food.entities.Restaurant;

@Repository
public interface FoodMenuRepository extends JpaRepository<FoodMenu, Integer>{

	FoodMenu findByName(String name);

	List<FoodMenu> findByRestaurant(Restaurant selectedRestaurant);
}