package com.incture.food.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.incture.food.dao.FoodMenuRepository;
import com.incture.food.entities.FoodMenu;
import com.incture.food.entities.Restaurant;

@ExtendWith(MockitoExtension.class)
public class FoodMenuServiceTest {

    @Mock
    private FoodMenuRepository foodMenuRepository;

    @InjectMocks
    private FoodMenuService foodMenuService;

    private FoodMenu foodMenu;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        
        foodMenu = new FoodMenu();
        foodMenu.setId(1);
        foodMenu.setName("Pizza");
        foodMenu.setPrice(10.99);
        foodMenu.setRestaurant(restaurant);
    }

    @Test
    void getAllFood_ShouldReturnAllFoodItems() {
        List<FoodMenu> expectedFood = Arrays.asList(foodMenu);
        when(foodMenuRepository.findAll()).thenReturn(expectedFood);

        List<FoodMenu> actualFood = foodMenuService.getAllFood();

        assertEquals(1, actualFood.size());
        assertEquals("Pizza", actualFood.get(0).getName());
        verify(foodMenuRepository, times(1)).findAll();
    }

    @Test
    void addFood_ShouldSaveFoodItem() {
        when(foodMenuRepository.save(any(FoodMenu.class))).thenReturn(foodMenu);

        FoodMenu savedFood = foodMenuService.addFood(foodMenu);

        assertNotNull(savedFood);
        assertEquals(1, savedFood.getId());
        verify(foodMenuRepository, times(1)).save(foodMenu);
    }

    @Test
    void addFood_ShouldAddToRestaurantMenu() {
        when(foodMenuRepository.save(any(FoodMenu.class))).thenReturn(foodMenu);

        FoodMenu savedFood = foodMenuService.addFood(foodMenu);

        assertTrue(restaurant.getFoodMenu().contains(savedFood));
        verify(foodMenuRepository, times(1)).save(foodMenu);
    }

    @Test
    void getFood_ShouldReturnFoodItem() {
        when(foodMenuRepository.findById(1)).thenReturn(Optional.of(foodMenu));

        FoodMenu foundFood = foodMenuService.getFood(1);

        
        assertEquals("Pizza", foundFood.getName());
        verify(foodMenuRepository, times(1)).findById(1);
    }

    @Test
    void getFood_ShouldThrowExceptionWhenNotFound() {
        when(foodMenuRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> foodMenuService.getFood(99));
        verify(foodMenuRepository, times(1)).findById(99);
    }

    @Test
    void updateFood_ShouldUpdateExistingItem() {
        FoodMenu updatedFood = new FoodMenu();
        updatedFood.setName("Updated Pizza");
        updatedFood.setPrice(12.99);
        
        when(foodMenuRepository.save(any(FoodMenu.class))).thenReturn(updatedFood);
        when(foodMenuRepository.existsById(1)).thenReturn(true);

        FoodMenu result = foodMenuService.updateFood(updatedFood, 1);

        assertEquals("Updated Pizza", result.getName());
        assertEquals(12.99, result.getPrice());
        assertEquals(1, result.getId());
        verify(foodMenuRepository, times(1)).save(updatedFood);
    }

    @Test
    void deleteFood_ShouldDeleteItem() {
        doNothing().when(foodMenuRepository).deleteById(1);
        when(foodMenuRepository.existsById(1)).thenReturn(true);

        foodMenuService.deleteFood(1);

        verify(foodMenuRepository, times(1)).deleteById(1);
    }
}