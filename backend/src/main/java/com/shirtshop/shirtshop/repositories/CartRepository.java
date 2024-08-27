package com.shirtshop.shirtshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shirtshop.shirtshop.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
	
	
}
