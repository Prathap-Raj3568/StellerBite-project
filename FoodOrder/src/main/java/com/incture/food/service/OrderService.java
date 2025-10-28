package com.incture.food.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.food.dao.OrderRepository;
import com.incture.food.entities.Order;

@Service
public class OrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private OrderRepository orderRepository;

	public List<Order> getAllOrders() {
	    logger.info("Fetching all orders");
	    return orderRepository.findAll();
	}

	public Order addOrder(Order order) {
	    logger.info("Adding new order: {}", order);
	    if (order.getDelivery() != null) {
	        logger.debug("Setting order for delivery with ID: {}", order.getDelivery().getId());
	        order.getDelivery().setOrder(order);
	    }
	    if (order.getPayment() != null) {
	        logger.debug("Setting order for payment with ID: {}", order.getPayment().getId());
	        order.getPayment().setOrder(order);
	    }
	    if (order.getRestaurant() != null) {
	        logger.debug("Initializing food menu for restaurant with ID: {}", order.getRestaurant().getId());
	        order.getRestaurant().getFoodMenu().size(); 
	    }
	    Order savedOrder = orderRepository.save(order);
	    logger.info("Order added successfully with ID: {}", savedOrder.getId());
	    return savedOrder;
	}

	public Order getOrder(int id) {
	    logger.debug("Fetching order by ID: {}", id);
	    return orderRepository.findById(id)
	            .orElseThrow(() -> {
	                logger.error("Order not found with ID: {}", id);
	                return new RuntimeException("Order not found with id: " + id);
	            });
	}

	public Order updateOrder(Order order, int id) {
	    logger.info("Updating order with ID: {}", id);
	    order.setId(id);
	    if (order.getDelivery() != null) {
	        logger.debug("Updating delivery for order with ID: {}", id);
	        order.getDelivery().setOrder(order);
	    }
	    if (order.getPayment() != null) {
	        logger.debug("Updating payment for order with ID: {}", id);
	        order.getPayment().setOrder(order);
	    }
	    if (order.getRestaurant() != null) {
	        logger.debug("Initializing food menu for restaurant in order with ID: {}", id);
	        order.getRestaurant().getFoodMenu().size();
	    }
	    Order updatedOrder = orderRepository.save(order);
	    logger.info("Order updated successfully with ID: {}", id);
	    return updatedOrder;
	}

	public void deleteOrder(int id) {
	    logger.info("Deleting order with ID: {}", id);
	    orderRepository.deleteById(id);
	    logger.info("Order deleted successfully with ID: {}", id);
	}
	}