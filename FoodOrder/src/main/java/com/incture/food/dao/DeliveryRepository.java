package com.incture.food.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incture.food.entities.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {

}