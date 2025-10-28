package com.incture.food.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incture.food.entities.FoodMenu;
import com.incture.food.service.FoodMenuService;

@ExtendWith(MockitoExtension.class)
public class FoodMenuControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private FoodMenuService foodMenuService;

    @InjectMocks
    private FoodMenuController foodMenuController;

    private FoodMenu foodMenu;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(foodMenuController).build();
        
        foodMenu = new FoodMenu();
        foodMenu.setId(1);
        foodMenu.setName("Burger");
        foodMenu.setPrice(8.99);
    }

    @Test
    void getAllFood_ShouldReturnAllFoodItems() throws Exception {
        List<FoodMenu> foodList = Arrays.asList(foodMenu);
        when(foodMenuService.getAllFood()).thenReturn(foodList);

        mockMvc.perform(get("/food_api/food"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].name").value("Burger"));
        
        verify(foodMenuService, times(1)).getAllFood();
    }

    @Test
    void getFood_ShouldReturnFoodItem() throws Exception {
        when(foodMenuService.getFood(1)).thenReturn(foodMenu);

        mockMvc.perform(get("/food_api/food/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Burger"));
        
        verify(foodMenuService, times(1)).getFood(1);
    }

    @Test
    void addFood_ShouldCreateNewFoodItem() throws Exception {
        when(foodMenuService.addFood(any(FoodMenu.class))).thenReturn(foodMenu);

        mockMvc.perform(post("/food_api/food")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(foodMenu)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Burger"));
        
        verify(foodMenuService, times(1)).addFood(any(FoodMenu.class));
    }

    @Test
    void updateFood_ShouldUpdateExistingItem() throws Exception {
        when(foodMenuService.updateFood(any(FoodMenu.class), eq(1))).thenReturn(foodMenu);

        mockMvc.perform(put("/food_api/food/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(foodMenu)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Burger"));
        
        verify(foodMenuService, times(1)).updateFood(any(FoodMenu.class), eq(1));
    }

    @Test
    void deleteFood_ShouldDeleteItem() throws Exception {
        doNothing().when(foodMenuService).deleteFood(1);

        mockMvc.perform(delete("/food_api/food/1"))
               .andExpect(status().isNoContent());
        
        verify(foodMenuService, times(1)).deleteFood(1);
    }
}