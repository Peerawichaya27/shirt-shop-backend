package com.shirtshop.shirtshop.services;

import com.shirtshop.shirtshop.dto.CartDTO;
import com.shirtshop.shirtshop.entity.CartItem;

public interface CartItemService {

	public CartItem createItemforCart(CartDTO cartdto);

}
