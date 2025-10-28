package com.incture.food;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.incture.food.dao.*;
import com.incture.food.entities.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

@SpringBootApplication
public class FoodOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodOrderApplication.class, args);
    }

    @Bean
    CommandLineRunner seedData(RoleRepository roleRepository, UserRepository userRepository,
                             RestaurantRepository restaurantRepository, OrderRepository orderRepository,
                             FoodMenuRepository foodMenuRepository, DeliveryRepository deliveryRepository,
                             OrderItemRepository orderItemRepository, PaymentRepository paymentRepository,
                             PasswordEncoder passwordEncoder) {
        return args -> {
            initializeRoles(roleRepository);
            
            Scanner scanner = new Scanner(System.in);
            
            while (true) {
                System.out.println("\nWelcome to Food Ordering System");
                System.out.println("1. Sign In");
                System.out.println("2. Sign Up");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                
                int mainChoice = getValidInteger(scanner, 1, 3);
                
                if (mainChoice == 3) {
                    System.out.println("Exiting application...");
                    break;
                }
                
                if (mainChoice == 1 || mainChoice == 2) {
                    User currentUser = handleAuthentication(mainChoice, scanner, userRepository, roleRepository, passwordEncoder);
                    
                    if (currentUser != null) {
                        if (hasRole(currentUser, "ROLE_Admin")) {
                            adminMenu(scanner, currentUser, restaurantRepository, foodMenuRepository, 
                                      orderRepository, userRepository, orderItemRepository, 
                                      paymentRepository, deliveryRepository, roleRepository, passwordEncoder);
                        } else {
                            userMenu(scanner, currentUser, restaurantRepository, foodMenuRepository, 
                                     orderRepository, orderItemRepository, paymentRepository, 
                                     deliveryRepository, userRepository);
                        }
                    }
                }
            }
            
            scanner.close();
            System.out.println("Application closed at " + LocalDateTime.now());
        };
    }
    
    private void initializeRoles(RoleRepository roleRepository) {
        String[] roles = {"ROLE_Admin", "ROLE_User"};
        for (String roleName : roles) {
            if (roleRepository.findByRoleName(roleName) == null) {
                Role role = new Role();
                role.setRoleName(roleName);
                roleRepository.save(role);
            }
        }
    }
    
    private User handleAuthentication(int choice, Scanner scanner, UserRepository userRepository, 
                                     RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        if (choice == 1) {
            return signIn(scanner, userRepository, passwordEncoder);
        } else {
            return signUp(scanner, userRepository, roleRepository, passwordEncoder);
        }
    }
    
    private User signIn(Scanner scanner, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        System.out.println("\n--- Sign In ---");
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("Invalid email or password!");
            return null;
        }
        
        System.out.println("Welcome back, " + user.getName() + "!");
        return user;
    }
    
    private User signUp(Scanner scanner, UserRepository userRepository, RoleRepository roleRepository, 
                       PasswordEncoder passwordEncoder) {
        System.out.println("\n--- Sign Up ---");
        
        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        while (email.isEmpty() || !email.contains("@") || userRepository.findByEmail(email) != null) {
            if (userRepository.findByEmail(email) != null) {
                System.out.print("Email already exists. Enter a different email: ");
            } else {
                System.out.print("Invalid email. Enter a valid email: ");
            }
            email = scanner.nextLine().trim();
        }
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        while (password.isEmpty()) {
            System.out.print("Password cannot be empty. Enter a valid password: ");
            password = scanner.nextLine().trim();
        }
        
        System.out.print("Enter phone number: ");
        String phoneInput = scanner.nextLine().trim();
        Long phone = phoneInput.isEmpty() ? null : getValidLong(scanner, phoneInput);
        
        System.out.print("Enter address: ");
        String address = scanner.nextLine().trim();
        
        System.out.println("Select role:");
        System.out.println("1. Admin");
        System.out.println("2. User");
        int roleChoice = getValidInteger(scanner, 1, 2);
        
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setAddress(address.isEmpty() ? "Default Address" : address);
        user.setPaymentMethod("Credit"); 
        
        Set<Role> roles = new HashSet<>();
        Role role = roleChoice == 1 ? 
            roleRepository.findByRoleName("ROLE_Admin") : 
            roleRepository.findByRoleName("ROLE_User");
        roles.add(role);
        user.setRoles(roles);
        
        userRepository.save(user);
        System.out.println("Registration successful! Welcome, " + name + "!");
        return user;
    }
    
    private void adminMenu(Scanner scanner, User admin, RestaurantRepository restaurantRepository, 
                          FoodMenuRepository foodMenuRepository, OrderRepository orderRepository,
                          UserRepository userRepository, OrderItemRepository orderItemRepository,
                          PaymentRepository paymentRepository, DeliveryRepository deliveryRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        while (true) {
            System.out.println("\n--- Admin Dashboard ---");
            System.out.println("1. Manage Restaurants");
            System.out.println("2. Manage Food Menu");
            System.out.println("3. View All Orders");
            System.out.println("4. Manage Delivery Agents");
            System.out.println("5. View System Users");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = getValidInteger(scanner, 1, 6);
            
            switch (choice) {
                case 1:
                    manageRestaurants(scanner, admin, restaurantRepository);
                    break;
                case 2:
                    manageFoodMenu(scanner, restaurantRepository, foodMenuRepository);
                    break;
                case 3:
                    viewAllOrders(orderRepository);
                    break;
                case 4:
                    manageDeliveryAgents(scanner, userRepository, roleRepository, passwordEncoder);
                    break;
                case 5:
                    viewSystemUsers(userRepository);
                    break;
                case 6:
                    return;
            }
        }
    }
    
    private void manageRestaurants(Scanner scanner, User admin, RestaurantRepository restaurantRepository) {
        System.out.println("\n--- Manage Restaurants ---");
        System.out.println("1. Add New Restaurant");
        System.out.println("2. View My Restaurants");
        System.out.println("3. Back");
        System.out.print("Choose an option: ");
        
        int choice = getValidInteger(scanner, 1, 3);
        
        switch (choice) {
            case 1:
                addRestaurant(scanner, admin, restaurantRepository);
                break;
            case 2:
                viewMyRestaurants(admin, restaurantRepository);
                break;
            case 3:
                return;
        }
    }
    
    private void addRestaurant(Scanner scanner, User admin, RestaurantRepository restaurantRepository) {
        System.out.println("\n--- Add New Restaurant ---");

        System.out.print("Enter restaurant name: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            System.out.print("Name cannot be empty. Enter restaurant name: ");
            name = scanner.nextLine().trim();
        }

        System.out.print("Enter restaurant email: ");
        String email = scanner.nextLine().trim().toLowerCase();
        while (email.isEmpty() || !email.contains("@")) {
            System.out.print("Invalid email. Enter restaurant email: ");
            email = scanner.nextLine().trim().toLowerCase();
        }

       
        System.out.println("Checking if email exists: '" + email + "'");
        Restaurant existing = restaurantRepository.findByEmailIgnoreCase(email);
        if (existing != null) {
            System.out.println("A restaurant with this email already exists!");
            return;
        }

        System.out.print("Enter restaurant location: ");
        String location = scanner.nextLine().trim();

        System.out.print("Enter restaurant rating (0.0 to 5.0): ");
        double rating = getValidDouble(scanner, 0.0, 5.0);

        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setEmail(email);
        restaurant.setLocation(location.isEmpty() ? "Default Location" : location);
        restaurant.setRating(rating);
        restaurant.setOwner(admin);

        try {
            restaurantRepository.save(restaurant);
            System.out.println("Restaurant added successfully!");
        } catch (Exception e) {
            System.out.println("Error saving restaurant: " + e.getMessage());
        }
    }
    
    private void viewMyRestaurants(User admin, RestaurantRepository restaurantRepository) {
        List<Restaurant> restaurants = restaurantRepository.findByOwner(admin);
        if (restaurants.isEmpty()) {
            System.out.println("You don't own any restaurants yet.");
        } else {
            System.out.println("\nYour Restaurants:");
            for (Restaurant restaurant : restaurants) {
                System.out.println("ID: " + restaurant.getId() + 
                                 ", Name: " + restaurant.getName() + 
                                 ", Location: " + restaurant.getLocation() + 
                                 ", Rating: " + restaurant.getRating());
            }
        }
    }
    
    private void manageFoodMenu(Scanner scanner, RestaurantRepository restaurantRepository, 
                               FoodMenuRepository foodMenuRepository) {
        System.out.println("\n--- Manage Food Menu ---");
        System.out.println("1. Add Food Item");
        System.out.println("2. View Food Items");
        System.out.println("3. Back");
        System.out.print("Choose an option: ");
        
        int choice = getValidInteger(scanner, 1, 3);
        
        switch (choice) {
            case 1:
                addFoodItem(scanner, restaurantRepository, foodMenuRepository);
                break;
            case 2:
                viewFoodItems(foodMenuRepository);
                break;
            case 3:
                return;
        }
    }
    
    private void addFoodItem(Scanner scanner, RestaurantRepository restaurantRepository, 
                            FoodMenuRepository foodMenuRepository) {
        System.out.println("\n--- Add Food Item ---");
        
        System.out.print("Enter restaurant name to add food item: ");
        String restaurantName = scanner.nextLine().trim();
        Restaurant restaurant = restaurantRepository.findByName(restaurantName);
        
        while (restaurant == null) {
            System.out.print("Restaurant not found. Enter valid restaurant name: ");
            restaurantName = scanner.nextLine().trim();
            restaurant = restaurantRepository.findByName(restaurantName);
        }
        
        System.out.print("Enter food item name: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            System.out.print("Name cannot be empty. Enter food item name: ");
            name = scanner.nextLine().trim();
        }
        
        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();
        
        System.out.print("Enter price: ");
        double price = getValidDouble(scanner, 0.0, Double.MAX_VALUE);
        
        System.out.print("Enter availability (Available/Unavailable): ");
        String availability = scanner.nextLine().trim();
        
        FoodMenu foodMenu = new FoodMenu();
        foodMenu.setName(name);
        foodMenu.setDescription(description.isEmpty() ? "No description" : description);
        foodMenu.setPrice(price);
        foodMenu.setAvailability(availability.isEmpty() ? "Available" : availability);
        foodMenu.setRestaurant(restaurant);
        
        foodMenuRepository.save(foodMenu);
        System.out.println("Food item added successfully to " + restaurant.getName() + "!");
    }
    
    private void viewFoodItems(FoodMenuRepository foodMenuRepository) {
        List<FoodMenu> foodItems = foodMenuRepository.findAll();
        if (foodItems.isEmpty()) {
            System.out.println("No food items found.");
        } else {
            System.out.println("\nAvailable Food Items:");
            for (FoodMenu item : foodItems) {
                System.out.println("ID: " + item.getId() + 
                                 ", Name: " + item.getName() + 
                                 ", Price: " + item.getPrice() + 
                                 ", Availability: " + item.getAvailability() + 
                                 ", Restaurant: " + item.getRestaurant().getName());
            }
        }
    }
    
    private void viewAllOrders(OrderRepository orderRepository) {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            System.out.println("\nAll Orders:");
            for (Order order : orders) {
                System.out.println("Order ID: " + order.getId() + 
                                 ", User: " + order.getUser().getName() + 
                                 ", Restaurant: " + order.getRestaurant().getName() + 
                                 ", Total: " + order.getTotalAmount() + 
                                 ", Status: " + order.getStatus());
            }
        }
    }
    
    private void manageDeliveryAgents(Scanner scanner, UserRepository userRepository, 
                                    RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        System.out.println("\n--- Manage Delivery Agents ---");
        System.out.println("1. Add New Delivery Agent");
        System.out.println("2. View All Delivery Agents");
        System.out.println("3. Back");
        System.out.print("Choose an option: ");
        
        int choice = getValidInteger(scanner, 1, 3);
        
        switch (choice) {
            case 1:
                addDeliveryAgent(scanner, userRepository, roleRepository, passwordEncoder);
                break;
            case 2:
                viewDeliveryAgents(userRepository);
                break;
            case 3:
                return;
        }
    }
    
    private void addDeliveryAgent(Scanner scanner, UserRepository userRepository, 
                                 RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        System.out.println("\n--- Add Delivery Agent ---");
        
        System.out.print("Enter agent name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        while (email.isEmpty() || !email.contains("@") || userRepository.findByEmail(email) != null) {
            if (userRepository.findByEmail(email) != null) {
                System.out.print("Email already exists. Enter a different email: ");
            } else {
                System.out.print("Invalid email. Enter a valid email: ");
            }
            email = scanner.nextLine().trim();
        }
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        while (password.isEmpty()) {
            System.out.print("Password cannot be empty. Enter a valid password: ");
            password = scanner.nextLine().trim();
        }
        
        System.out.print("Enter phone number: ");
        String phoneInput = scanner.nextLine().trim();
        Long phone = phoneInput.isEmpty() ? null : getValidLong(scanner, phoneInput);
        
        User agent = new User();
        agent.setName(name);
        agent.setEmail(email);
        agent.setPassword(passwordEncoder.encode(password));
        agent.setPhone(phone);
        agent.setAddress("Not specified");
        agent.setPaymentMethod("Cash");
        
        Role deliveryRole = roleRepository.findByRoleName("ROLE_Delivery");
        if (deliveryRole == null) {
            deliveryRole = new Role();
            deliveryRole.setRoleName("ROLE_Delivery");
            roleRepository.save(deliveryRole);
        }
        
        Set<Role> roles = new HashSet<>();
        roles.add(deliveryRole);
        agent.setRoles(roles);
        
        userRepository.save(agent);
        System.out.println("Delivery agent added successfully!");
    }
    
    private void viewDeliveryAgents(UserRepository userRepository) {
        List<User> agents = userRepository.findByRoles_RoleName("ROLE_Delivery");
        if (agents.isEmpty()) {
            System.out.println("No delivery agents found.");
        } else {
            System.out.println("\nDelivery Agents:");
            for (User agent : agents) {
                System.out.println("ID: " + agent.getId() + 
                                 ", Name: " + agent.getName() + 
                                 ", Email: " + agent.getEmail() + 
                                 ", Phone: " + agent.getPhone());
            }
        }
    }
    
    private void viewSystemUsers(UserRepository userRepository) {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("\nSystem Users:");
            for (User user : users) {
                System.out.println("ID: " + user.getId() + 
                                 ", Name: " + user.getName() + 
                                 ", Email: " + user.getEmail() + 
                                 ", Roles: " + getRoleNames(user.getRoles()));
            }
        }
    }
    
    private String getRoleNames(Set<Role> roles) {
        StringBuilder sb = new StringBuilder();
        for (Role role : roles) {
            sb.append(role.getRoleName()).append(", ");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "No roles";
    }
    
    private void userMenu(Scanner scanner, User user, RestaurantRepository restaurantRepository, 
                         FoodMenuRepository foodMenuRepository, OrderRepository orderRepository,
                         OrderItemRepository orderItemRepository, PaymentRepository paymentRepository,
                         DeliveryRepository deliveryRepository, UserRepository userRepository) {
        while (true) {
            System.out.println("\n--- User Dashboard ---");
            System.out.println("1. View Restaurants");
            System.out.println("2. Place Order");
            System.out.println("3. View My Orders");
            System.out.println("4. Update Profile");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = getValidInteger(scanner, 1, 5);
            
            switch (choice) {
                case 1:
                    viewAllRestaurants(restaurantRepository);
                    break;
                case 2:
                    placeOrder(scanner, user, restaurantRepository, foodMenuRepository, 
                             orderRepository, orderItemRepository, paymentRepository, 
                             deliveryRepository, userRepository);
                    break;
                case 3:
                    viewMyOrders(user, orderRepository);
                    break;
                case 4:
                    updateProfile(scanner, user, userRepository);
                    break;
                case 5:
                    return;
            }
        }
    }
    
    private void viewAllRestaurants(RestaurantRepository restaurantRepository) {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        if (restaurants.isEmpty()) {
            System.out.println("No restaurants found.");
        } else {
            System.out.println("\nAvailable Restaurants:");
            for (Restaurant restaurant : restaurants) {
                System.out.println("ID: " + restaurant.getId() + 
                                 ", Name: " + restaurant.getName() + 
                                 ", Location: " + restaurant.getLocation() + 
                                 ", Rating: " + restaurant.getRating());
            }
        }
    }
    
    private void placeOrder(Scanner scanner, User user, RestaurantRepository restaurantRepository, 
                          FoodMenuRepository foodMenuRepository, OrderRepository orderRepository,
                          OrderItemRepository orderItemRepository, PaymentRepository paymentRepository,
                          DeliveryRepository deliveryRepository, UserRepository userRepository) {
        System.out.println("\n--- Place New Order ---");
        
        System.out.println("Available Restaurants:");
        List<Restaurant> restaurants = restaurantRepository.findAll();
        if (restaurants.isEmpty()) {
            System.out.println("No restaurants available to order from.");
            return;
        }
        
        for (int i = 0; i < restaurants.size(); i++) {
            System.out.println((i+1) + ". " + restaurants.get(i).getName() + 
                             " (" + restaurants.get(i).getLocation() + ")");
        }
        
        System.out.print("Select restaurant (number): ");
        int restaurantChoice = getValidInteger(scanner, 1, restaurants.size());
        Restaurant selectedRestaurant = restaurants.get(restaurantChoice - 1);
        
        System.out.println("\nMenu for " + selectedRestaurant.getName() + ":");
        List<FoodMenu> menuItems = foodMenuRepository.findByRestaurant(selectedRestaurant);
        if (menuItems.isEmpty()) {
            System.out.println("No items available in this restaurant's menu.");
            return;
        }
        
        for (int i = 0; i < menuItems.size(); i++) {
            FoodMenu item = menuItems.get(i);
            System.out.println((i+1) + ". " + item.getName() + 
                             " - " + item.getDescription() + 
                             " - $" + item.getPrice() + 
                             " (" + item.getAvailability() + ")");
        }
        
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;
        
        while (true) {
            System.out.print("\nSelect item to add to order (number) or 0 to finish: ");
            int itemChoice = getValidInteger(scanner, 0, menuItems.size());
            
            if (itemChoice == 0) {
                if (orderItems.isEmpty()) {
                    System.out.println("You must select at least one item to place an order.");
                    continue;
                }
                break;
            }
            
            FoodMenu selectedItem = menuItems.get(itemChoice - 1);
            if (selectedItem.getAvailability().equalsIgnoreCase("Unavailable")) {
                System.out.println("This item is currently unavailable. Please choose another.");
                continue;
            }
            
            System.out.print("Enter quantity for " + selectedItem.getName() + ": ");
            int quantity = getValidInteger(scanner, 1, 100);
            
            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(selectedItem);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(selectedItem.getPrice());
            
            orderItems.add(orderItem);
            totalAmount += selectedItem.getPrice() * quantity;
            
            System.out.println("Added " + quantity + " x " + selectedItem.getName() + " to your order.");
        }
        
        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(selectedRestaurant);
        order.setDateTime(LocalDateTime.now());
        order.setTotalAmount(totalAmount);
        order.setStatus("PLACED");
        order.setOrderItem(new ArrayList<>());
        
        order = orderRepository.save(order);
        
        for (OrderItem item : orderItems) {
            item.setOrder(order);
            orderItemRepository.save(item);
            order.getOrderItem().add(item);
        }
        
        System.out.println("\n--- Payment ---");
        System.out.println("Total amount: $" + totalAmount);
        System.out.print("Select payment method (Credit/Cash): ");
        String paymentMethod = scanner.nextLine().trim();
        paymentMethod = paymentMethod.isEmpty() ? user.getPaymentMethod() : paymentMethod;
        
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(paymentMethod);
        payment.setStatus("COMPLETED");
        paymentRepository.save(payment);
        
        System.out.println("\n--- Delivery ---");
        List<User> deliveryAgents = userRepository.findByRoles_RoleName("ROLE_Delivery");
        if (!deliveryAgents.isEmpty()) {
            User deliveryAgent = deliveryAgents.get(0); 
            LocalDateTime estimatedTime = LocalDateTime.now().plusMinutes(30);
            
            Delivery delivery = new Delivery();
            delivery.setOrder(order);
            delivery.setDeliveryAgent(deliveryAgent);
            delivery.setEstimatedTime(estimatedTime);
            delivery.setStatus("PENDING");
            deliveryRepository.save(delivery);
            
            System.out.println("Your order will be delivered by " + deliveryAgent.getName() + 
                             ", estimated delivery time: " + estimatedTime);
        } else {
            System.out.println("No delivery agents available currently. Your order will be processed soon.");
        }
        
        System.out.println("\nOrder placed successfully! Order ID: " + order.getId());
    }
    
    private void viewMyOrders(User user, OrderRepository orderRepository) {
        List<Order> orders = orderRepository.findByUser(user);
        if (orders.isEmpty()) {
            System.out.println("You have no orders yet.");
        } else {
            System.out.println("\nYour Orders:");
            for (Order order : orders) {
                System.out.println("Order ID: " + order.getId() + 
                                 ", Restaurant: " + order.getRestaurant().getName() + 
                                 ", Total: $" + order.getTotalAmount() + 
                                 ", Status: " + order.getStatus() + 
                                 ", Date: " + order.getDateTime());
            }
        }
    }
    
    private void updateProfile(Scanner scanner, User user, UserRepository userRepository) {
        System.out.println("\n--- Update Profile ---");
        
        System.out.print("Current name: " + user.getName() + ". Enter new name (or press enter to keep current): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            user.setName(name);
        }
        
        System.out.print("Current phone: " + user.getPhone() + ". Enter new phone (or press enter to keep current): ");
        String phoneInput = scanner.nextLine().trim();
        if (!phoneInput.isEmpty()) {
            user.setPhone(getValidLong(scanner, phoneInput));
        }
        
        System.out.print("Current address: " + user.getAddress() + ". Enter new address (or press enter to keep current): ");
        String address = scanner.nextLine().trim();
        if (!address.isEmpty()) {
            user.setAddress(address);
        }
        
        System.out.print("Current payment method: " + user.getPaymentMethod() + ". Enter new method (or press enter to keep current): ");
        String paymentMethod = scanner.nextLine().trim();
        if (!paymentMethod.isEmpty()) {
            user.setPaymentMethod(paymentMethod);
        }
        
        userRepository.save(user);
        System.out.println("Profile updated successfully!");
    }
    
    private boolean hasRole(User user, String roleName) {
        return user.getRoles().stream().anyMatch(role -> role.getRoleName().equals(roleName));
    }
    
    private int getValidInteger(Scanner scanner, int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) return value;
                else System.out.print("Please enter a number between " + min + " and " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a valid number: ");
            }
        }
    }
    
    private Long getValidLong(Scanner scanner, String input) {
        while (true) {
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Enter a valid number: ");
                input = scanner.nextLine().trim();
            }
        }
    }
    
    private double getValidDouble(Scanner scanner, double min, double max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                double value = Double.parseDouble(input);
                if (value >= min && value <= max) return value;
                else System.out.print("Please enter a number between " + min + " and " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a valid number: ");
            }
        }
    }
}