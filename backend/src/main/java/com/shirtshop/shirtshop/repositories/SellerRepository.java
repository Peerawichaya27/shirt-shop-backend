package com.shirtshop.shirtshop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shirtshop.shirtshop.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
	
	Optional<Seller> findByUsername(String usernaame);
	
}
