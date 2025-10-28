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

import com.incture.food.entities.OrderItem;
import com.incture.food.service.OrderItemService;

@RestController
@RequestMapping("food_api")
public class OrderItemController {
	private static final Logger logger = LoggerFactory.getLogger(OrderItemController.class);

	@Autowired
	private OrderItemService orderItemService;

	@GetMapping("/order_item")
	public ResponseEntity<List<OrderItem>> getAllOrderItems() {
	    logger.info("GET /order_item - Fetching all order items");
	    return ResponseEntity.ok(orderItemService.getAllOrderItems());
	}

	@GetMapping("/order_item/{id}")
	public ResponseEntity<OrderItem> getOrderItem(@PathVariable int id) {
	    logger.info("GET /order_item/{} - Fetching order item", id);
	    return ResponseEntity.ok(orderItemService.getOrderItem(id));
	}

	@PostMapping("/order_item")
	public ResponseEntity<OrderItem> addOrderItem(@RequestBody OrderItem orderItem) {
	    logger.info("POST /order_item - Adding new order item");
	    return ResponseEntity.ok(orderItemService.addOrderItem(orderItem));
	}

	@PutMapping("/order_item/{id}")
	public ResponseEntity<OrderItem> updateOrderItem(@RequestBody OrderItem orderItem, @PathVariable int id) {
	    logger.info("PUT /order_item/{} - Updating order item", id);
	    return ResponseEntity.ok(orderItemService.updateOrderItem(orderItem, id));
	}

	@DeleteMapping("/order_item/{id}")
	public ResponseEntity<Void> deleteOrderItem(@PathVariable int id) {
	    logger.info("DELETE /order_item/{} - Deleting order item", id);
	    orderItemService.deleteOrderItem(id);
	    logger.info("Order item deleted successfully with ID: {}", id);
	    return ResponseEntity.noContent().build();
	}
	}