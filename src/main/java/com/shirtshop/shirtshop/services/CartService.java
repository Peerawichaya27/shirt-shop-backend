package com.shirtshop.shirtshop.services;

import com.shirtshop.shirtshop.dto.CartDTO;
import com.shirtshop.shirtshop.entity.Cart;

public interface CartService {

	public Cart addProductToCart(CartDTO cartDto, String token);

	public Cart getCartProduct(String token);

	public Cart removeProductFromCart(CartDTO cartDto, String token);

	public Cart clearCart(String token);

}
