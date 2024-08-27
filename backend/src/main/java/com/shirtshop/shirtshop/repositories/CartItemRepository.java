package com.shirtshop.shirtshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shirtshop.shirtshop.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

}
