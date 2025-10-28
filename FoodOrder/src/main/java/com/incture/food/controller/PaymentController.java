package com.incture.food.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incture.food.entities.Payment;
import com.incture.food.service.PaymentService;

@RestController
@RequestMapping("food_api")
public class PaymentController {
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	private PaymentService paymentService;

	@GetMapping("/payment/{payment_id}")
	public ResponseEntity<Payment> getPayment(@PathVariable("payment_id") int id) {
	    logger.info("GET /payment/{} - Fetching payment", id);
	    return ResponseEntity.ok(paymentService.getPayment(id));
	}

	@PostMapping("/payment")
	public ResponseEntity<Payment> processPayment(@RequestBody Payment payment) {
	    logger.info("POST /payment - Processing payment");
	    return ResponseEntity.ok(paymentService.processPayment(payment));
	}
	}