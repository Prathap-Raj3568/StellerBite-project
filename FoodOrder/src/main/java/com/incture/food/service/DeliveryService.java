package com.incture.food.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.food.dao.DeliveryRepository;
import com.incture.food.entities.Delivery;

@Service
public class DeliveryService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);
    
    @Autowired
    private DeliveryRepository deliveryRepository;
    
    public Delivery assignDelivery(Delivery delivery) {
        logger.info("Assigning new delivery: {}", delivery);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        logger.info("Delivery assigned successfully with ID: {}", savedDelivery.getId());
        return savedDelivery;
    }
    
    public Delivery updateDeliveryStatus(int deliveryId, String status) {
        logger.info("Updating delivery status for ID: {} to status: {}", deliveryId, status);
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setStatus(status);
        Delivery updatedDelivery = deliveryRepository.save(delivery);
        logger.info("Delivery status updated successfully for ID: {}", deliveryId);
        return updatedDelivery;
    }
    
    public Delivery getDeliveryById(int deliveryId) {
        logger.debug("Fetching delivery by ID: {}", deliveryId);
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> {
                    logger.error("Delivery not found with ID: {}", deliveryId);
                    return new RuntimeException("Delivery not found");
                });
    }
}