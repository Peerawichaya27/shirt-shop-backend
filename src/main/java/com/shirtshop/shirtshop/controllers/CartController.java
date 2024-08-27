package com.shirtshop.shirtshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.shirtshop.shirtshop.dto.CartDTO;
import com.shirtshop.shirtshop.entity.Cart;
import com.shirtshop.shirtshop.services.CartService;

@RestController
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@PostMapping("/cart/add")
	public ResponseEntity<Cart> addProductToCartHander(@RequestBody CartDTO cartdto ,@RequestHeader("Authorization")String token){
		token = token.substring("Bearer ".length());
		Cart cart = cartService.addProductToCart(cartdto, token);
		return new ResponseEntity<Cart>(cart,HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@GetMapping("/cart")
	public ResponseEntity<Cart> getCartProductHandler(@RequestHeader("Authorization")String token){
		token = token.substring("Bearer ".length());
		return new ResponseEntity<>(cartService.getCartProduct(token), HttpStatus.ACCEPTED);
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@DeleteMapping("/cart")
	public ResponseEntity<Cart> removeProductFromCartHander(@RequestBody CartDTO cartdto ,@RequestHeader("Authorization")String token){
		token = token.substring("Bearer ".length());
		Cart cart = cartService.removeProductFromCart(cartdto, token);
		return new ResponseEntity<Cart>(cart,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@DeleteMapping("/cart/clear")
	public ResponseEntity<Cart> clearCartHandler(@RequestHeader("Authorization") String token){
		token = token.substring("Bearer ".length());
		return new ResponseEntity<>(cartService.clearCart(token), HttpStatus.ACCEPTED);
	}
}
