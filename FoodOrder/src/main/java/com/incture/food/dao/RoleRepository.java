package com.incture.food.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incture.food.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByRoleName(String roleName);
}
