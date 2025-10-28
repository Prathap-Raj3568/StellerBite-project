package com.incture.food.entities;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private Set<User> users = new HashSet<>();

	public Long getRole_id() {
		return role_id;
	}

	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return roleName;
	}
}