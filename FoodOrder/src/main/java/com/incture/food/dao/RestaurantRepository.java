package com.incture.food.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.incture.food.entities.Restaurant;
import com.incture.food.entities.User;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

	Restaurant findByName(String name);
	Restaurant findByEmail(String email);
	List<Restaurant> findByOwner(User admin);
	Restaurant findByEmailIgnoreCase(String email);

}
