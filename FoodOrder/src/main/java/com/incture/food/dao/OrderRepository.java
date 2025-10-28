package com.incture.food.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incture.food.entities.Order;
import com.incture.food.entities.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(int userId);
    List<Order> findByRestaurantId(int restaurantId);
	List<Order> findByUser(User user);
}