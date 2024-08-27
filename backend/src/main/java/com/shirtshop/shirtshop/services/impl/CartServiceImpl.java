package com.shirtshop.shirtshop.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.shirtshop.shirtshop.dto.CartDTO;
import com.shirtshop.shirtshop.entity.Cart;
import com.shirtshop.shirtshop.entity.CartItem;
import com.shirtshop.shirtshop.entity.Customer;
import com.shirtshop.shirtshop.exception.CartItemNotFound;
import com.shirtshop.shirtshop.exception.CustomerNotFoundException;
import com.shirtshop.shirtshop.repositories.CartRepository;
import com.shirtshop.shirtshop.repositories.CustomerRepository;
import com.shirtshop.shirtshop.services.CartItemService;
import com.shirtshop.shirtshop.services.CartService;
import com.shirtshop.shirtshop.services.JwtService;

@Service
public class CartServiceImpl implements CartService  {
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private JwtService jwtService;
	
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	public Cart addProductToCart(CartDTO cartDto, String token) {
	    String username = jwtService.extractUsername(token);
	    Optional<Customer> user = customerRepository.findByUsername(username);

	    if (user.isEmpty()) {
	        throw new CustomerNotFoundException("Customer does not exist");
	    }

	    Customer existingCustomer = user.get();
	    Cart customerCart = existingCustomer.getCustomerCart();

	    // Check if the customer already has a cart, if not, create one
	    if (customerCart == null) {
	        customerCart = new Cart();
	        customerCart.setCustomer(existingCustomer);
	        existingCustomer.setCustomerCart(customerCart);
	    }

	    // Ensure cartTotal is not null
	    if (customerCart.getCartTotal() == null) {
	        customerCart.setCartTotal(0.0);
	    }

	    List<CartItem> cartItems = customerCart.getCartItems();
	    CartItem item = cartItemService.createItemforCart(cartDto);

	    boolean itemExistsInCart = false;
	    for (CartItem c : cartItems) {
	        if (c.getCartProduct().getProductId().equals(cartDto.getProductId())) {
	            // Update quantity if item exists
	            c.setCartItemQuantity(c.getCartItemQuantity() + cartDto.getQuantity());
	            customerCart.setCartTotal(customerCart.getCartTotal() + (c.getCartProduct().getPrice() * cartDto.getQuantity()));
	            itemExistsInCart = true;
	            break;
	        }
	    }

	    if (!itemExistsInCart) {
	        // Add the new item directly to the existing cartItems list
	        cartItems.add(item);
	        customerCart.setCartTotal(customerCart.getCartTotal() + (item.getCartProduct().getPrice() * item.getCartItemQuantity()));
	    }

	    return cartRepository.save(customerCart);
	}





	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	public Cart getCartProduct(String token) {
		System.out.println("Inside get cart");
		String username = jwtService.extractUsername(token);
		Optional<Customer> user = customerRepository.findByUsername(username);
		if(user.isEmpty())
			throw new CustomerNotFoundException("Customer does not exist");	
		Customer existingCustomer = user.get();
		Integer cartId = existingCustomer.getCustomerCart().getCartId();
		Optional<Cart> userCart= cartRepository.findById(cartId);
		if(userCart.isEmpty()) {
			throw new CartItemNotFound("cart Not found by Id");
		}
		return userCart.get();
	}

	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	public Cart removeProductFromCart(CartDTO cartDto, String token) {
	    String username = jwtService.extractUsername(token);
	    Optional<Customer> user = customerRepository.findByUsername(username);
	    if (user.isEmpty())
	        throw new CustomerNotFoundException("Customer does not exist");
	    Customer existingCustomer = user.get();
	    Cart customerCart = existingCustomer.getCustomerCart();
	    List<CartItem> cartItems = customerCart.getCartItems();
	    if (cartItems == null || cartItems.isEmpty()) {
	        throw new CartItemNotFound("Cart is empty");
	    }
	    boolean flag = false;
	    CartItem itemToRemove = null;
	    for (CartItem c : cartItems) {
	        if (c.getCartProduct().getProductId().equals(cartDto.getProductId())) {
	            c.setCartItemQuantity(c.getCartItemQuantity() - cartDto.getQuantity());
	            customerCart.setCartTotal(customerCart.getCartTotal() - (c.getCartProduct().getPrice() * cartDto.getQuantity()));
	            if (c.getCartItemQuantity() <= 0) {	
	                itemToRemove = c;
	            }
	            flag = true;
	            break;
	        }
	    }
	    if (itemToRemove != null) {
	        cartItems.remove(itemToRemove);
	    }
	    if (!flag) {
	        throw new CartItemNotFound("Product not added to cart");
	    }
	    if (cartItems.isEmpty()) {
	        customerCart.setCartTotal(0.0);
	        cartRepository.save(customerCart);
	        throw new CartItemNotFound("Cart is empty now");
	    }
	    return cartRepository.save(customerCart);
	}

	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	public Cart clearCart(String token) {
	    String username = jwtService.extractUsername(token);
	    Optional<Customer> user = customerRepository.findByUsername(username);
	    if (user.isEmpty())
	        throw new CustomerNotFoundException("Customer does not exist");

	    Customer existingCustomer = user.get();
	    Cart customerCart = existingCustomer.getCustomerCart();

	    if (customerCart.getCartItems().isEmpty()) {
	        throw new CartItemNotFound("Cart already empty");
	    }

	    // Clear the cartItems without replacing the collection
	    customerCart.getCartItems().clear();
	    customerCart.setCartTotal(0.0);

	    return cartRepository.save(customerCart);
	}


}
