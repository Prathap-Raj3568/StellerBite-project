package com.incture.food.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.food.dao.OrderItemRepository;
import com.incture.food.entities.OrderItem;

@Service
public class OrderItemService {
	private static final Logger logger = LoggerFactory.getLogger(OrderItemService.class);

	@Autowired
	private OrderItemRepository orderItemRepository;

	public List<OrderItem> getAllOrderItems() {
	    logger.info("Fetching all order items");
	    return orderItemRepository.findAll();
	}

	public OrderItem addOrderItem(OrderItem orderItem) {
	    logger.info("Adding new order item: {}", orderItem);
	    if (orderItem.getOrder() != null) {
	        logger.debug("Adding order item to order with ID: {}", orderItem.getOrder().getId());
	        orderItem.getOrder().getOrderItem().add(orderItem);
	    }
	    OrderItem savedItem = orderItemRepository.save(orderItem);
	    logger.info("Order item added successfully with ID: {}", savedItem.getId());
	    return savedItem;
	}

	public OrderItem getOrderItem(int id) {
	    logger.debug("Fetching order item by ID: {}", id);
	    return orderItemRepository.findById(id)
	            .orElseThrow(() -> {
	                logger.error("OrderItem not found with ID: {}", id);
	                return new RuntimeException("OrderItem not found with id: " + id);
	            });
	}

	public OrderItem updateOrderItem(OrderItem orderItem, int id) {
	    logger.info("Updating order item with ID: {}", id);
	    orderItem.setId(id);
	    if (orderItem.getOrder() != null) {
	        logger.debug("Updating order item in order with ID: {}", orderItem.getOrder().getId());
	        orderItem.getOrder().getOrderItem().add(orderItem);
	    }
	    OrderItem updatedItem = orderItemRepository.save(orderItem);
	    logger.info("Order item updated successfully with ID: {}", id);
	    return updatedItem;
	}

	public void deleteOrderItem(int id) {
	    logger.info("Deleting order item with ID: {}", id);
	    orderItemRepository.deleteById(id);
	    logger.info("Order item deleted successfully with ID: {}", id);
	}
	}