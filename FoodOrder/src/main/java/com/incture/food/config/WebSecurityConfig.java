package com.incture.food.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers(HttpMethod.POST, "/food_api/user/register").permitAll()
                .requestMatchers(HttpMethod.GET, "food_api").hasAnyRole("Customer", "Admin")
                .requestMatchers(HttpMethod.POST, "food_api").hasRole("Admin")
                .requestMatchers(HttpMethod.PUT, "food_api").hasRole("Admin")
                .requestMatchers(HttpMethod.DELETE, "food_api").hasRole("Admin")
                .anyRequest().authenticated());
        
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }
}