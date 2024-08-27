package com.shirtshop.shirtshop.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.shirtshop.shirtshop.dto.CartDTO;
import com.shirtshop.shirtshop.entity.CartItem;
import com.shirtshop.shirtshop.entity.Product;
import com.shirtshop.shirtshop.entity.ProductStatus;
import com.shirtshop.shirtshop.exception.ProductNotFoundException;
import com.shirtshop.shirtshop.repositories.ProductRepository;
import com.shirtshop.shirtshop.services.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService{
	
	@Autowired
	ProductRepository productRepository;
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	public CartItem createItemforCart(CartDTO cartdto) {
		Product existingProduct = productRepository.findById(cartdto.getProductId()).orElseThrow( () -> new ProductNotFoundException("Product Not found"));
		if(existingProduct.getStatus().equals(ProductStatus.OUTOFSTOCK) || existingProduct.getQuantity() == 0) {
			throw new ProductNotFoundException("Product OUT OF STOCK");
		}
		CartItem newItem = new CartItem();
		newItem.setCartItemQuantity(cartdto.getQuantity());
		newItem.setCartProduct(existingProduct);
		return newItem;
	}

}
