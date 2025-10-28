package com.incture.food.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime estimatedTime;
    private LocalDateTime actualTime;
    private String status;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "delivery_agent_id")
    private User deliveryAgent;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(LocalDateTime estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public LocalDateTime getActualTime() {
		return actualTime;
	}

	public void setActualTime(LocalDateTime actualTime) {
		this.actualTime = actualTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public User getDeliveryAgent() {
		return deliveryAgent;
	}

	public void setDeliveryAgent(User deliveryAgent) {
		this.deliveryAgent = deliveryAgent;
	}

	public Delivery() {
		super();
		// TODO Auto-generated constructor stub
	}
	
    
}