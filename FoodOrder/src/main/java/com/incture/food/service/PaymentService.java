package com.incture.food.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.food.dao.PaymentRepository;
import com.incture.food.entities.Payment;

@Service
public class PaymentService {
	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

	@Autowired
	private PaymentRepository paymentRepository;

	public Payment processPayment(Payment payment) {
	    logger.info("Processing payment: {}", payment);
	    Payment processedPayment = paymentRepository.save(payment);
	    logger.info("Payment processed successfully with ID: {}", processedPayment.getId());
	    return processedPayment;
	}

	public Payment getPayment(int id) {
	    logger.debug("Fetching payment by ID: {}", id);
	    return paymentRepository.findById(id)
	            .orElseThrow(() -> {
	                logger.error("Payment not found with ID: {}", id);
	                return new RuntimeException("Payment not found with id: " + id);
	            });
	}
	}