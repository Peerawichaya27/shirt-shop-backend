package com.shirtshop.shirtshop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shirtshop.shirtshop.entity.Users;


public interface UserRepository extends JpaRepository<Users, Integer> {
	
	Optional<Users> findByUsername(String username);

	
}
