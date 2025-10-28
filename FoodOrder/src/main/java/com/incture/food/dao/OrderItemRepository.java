package com.incture.food.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incture.food.entities.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}