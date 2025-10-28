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

import com.incture.food.entities.Order;
import com.incture.food.service.OrderService;

@RestController
@RequestMapping("food_api")
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;

	@GetMapping("/order")
	public ResponseEntity<List<Order>> getAllOrders() {
	    logger.info("GET /order - Fetching all orders");
	    return ResponseEntity.ok(orderService.getAllOrders());
	}

	@GetMapping("/order/{order_id}")
	public ResponseEntity<Order> getOrder(@PathVariable("order_id") int id) {
	    logger.info("GET /order/{} - Fetching order", id);
	    return ResponseEntity.ok(orderService.getOrder(id));
	}

	@PostMapping("/order")
	public ResponseEntity<Order> addOrder(@RequestBody Order order) {
	    logger.info("POST /order - Adding new order");
	    return ResponseEntity.ok(orderService.addOrder(order));
	}

	@PutMapping("/order/{order_id}")
	public ResponseEntity<Order> updateOrder(@RequestBody Order order, @PathVariable("order_id") int id) {
	    logger.info("PUT /order/{} - Updating order", id);
	    return ResponseEntity.ok(orderService.updateOrder(order, id));
	}

	@DeleteMapping("/order/{order_id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable("order_id") int id) {
	    logger.info("DELETE /order/{} - Deleting order", id);
	    orderService.deleteOrder(id);
	    logger.info("Order deleted successfully with ID: {}", id);
	    return ResponseEntity.noContent().build();
	}
	}