package com.incture.food.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurant", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "id"),
           @UniqueConstraint(columnNames = "email")
       })
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private double rating;

    @Column(unique = true, nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "restaurant", 
               cascade = CascadeType.ALL, 
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private Set<FoodMenu> foodMenu = new HashSet<>();

    public Restaurant() {
    }

    public Restaurant(String name, String location, double rating, String email, User owner) {
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.email = email;
        this.owner = owner;
    }

    public void addFoodMenu(FoodMenu item) {
        foodMenu.add(item);
        item.setRestaurant(this);
    }

    public void removeFoodMenu(FoodMenu item) {
        foodMenu.remove(item);
        item.setRestaurant(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<FoodMenu> getFoodMenu() {
        return foodMenu;
    }

    public void setFoodMenu(Set<FoodMenu> foodMenu) {
        this.foodMenu = foodMenu;
    }
}