package com.incture.food.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.food.entities.Delivery;
import com.incture.food.service.DeliveryService;

@RestController
@RequestMapping("food_api")
public class DeliveryController {
	private static final Logger logger = LoggerFactory.getLogger(DeliveryController.class);

	@Autowired
	private DeliveryService deliveryService;

	@GetMapping("/delivery/{delivery_id}")
	public ResponseEntity<Delivery> getDelivery(@PathVariable("delivery_id") int id) {
	    logger.info("GET /delivery/{} - Fetching delivery", id);
	    return ResponseEntity.ok(deliveryService.getDeliveryById(id));
	}

	@PostMapping("/delivery")
	public ResponseEntity<Delivery> assignDelivery(@RequestBody Delivery delivery) {
	    logger.info("POST /delivery - Assigning new delivery");
	    return ResponseEntity.ok(deliveryService.assignDelivery(delivery));
	}

	@PutMapping("/delivery/{delivery_id}")
	public ResponseEntity<Delivery> updateStatus(@PathVariable("delivery_id") int deliveryId, @RequestBody String status) {
	    logger.info("PUT /delivery/{} - Updating status to: {}", deliveryId, status);
	    Delivery updated = deliveryService.updateDeliveryStatus(deliveryId, status);
	    logger.info("Status updated successfully for delivery ID: {}", deliveryId);
	    return ResponseEntity.ok(updated);
	}
	}