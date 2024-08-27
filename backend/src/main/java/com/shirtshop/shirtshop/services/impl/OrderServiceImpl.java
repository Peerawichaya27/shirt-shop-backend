package com.shirtshop.shirtshop.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.shirtshop.shirtshop.dto.CartDTO;
import com.shirtshop.shirtshop.dto.OrderDTO;
import com.shirtshop.shirtshop.entity.CartItem;
import com.shirtshop.shirtshop.entity.CreditCard;
import com.shirtshop.shirtshop.entity.Customer;
import com.shirtshop.shirtshop.entity.Order;
import com.shirtshop.shirtshop.entity.OrderStatusValues;
import com.shirtshop.shirtshop.entity.ProductStatus;
import com.shirtshop.shirtshop.exception.LoginException;
import com.shirtshop.shirtshop.exception.OrderException;
import com.shirtshop.shirtshop.repositories.CustomerRepository;
import com.shirtshop.shirtshop.repositories.OrderRepository;
import com.shirtshop.shirtshop.services.CustomerService;
import com.shirtshop.shirtshop.services.JwtService;
import com.shirtshop.shirtshop.services.OrderService;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CartServiceImpl cartservicei;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	@Transactional
	public Order saveOrder(OrderDTO odto, String token) throws LoginException, OrderException {    
	    Order newOrder = new Order();    
	    String username = jwtService.extractUsername(token);
	    Optional<Customer> user = customerRepository.findByUsername(username);
	    Customer customer = user.orElseThrow(() -> new LoginException("Invalid session token for customer. Kindly Login"));

	    newOrder.setCustomer(customer);

	    CreditCard userStoredCard = customer.getCreditCard();
	    CreditCard userProvidedCard = odto.getCardNumber(); // This is now a CreditCard object

	    List<CartItem> productsInCart = customer.getCustomerCart().getCartItems();
	    if (productsInCart.isEmpty()) {
	        throw new OrderException("No products in Cart");
	    }

	    List<CartItem> productsInOrder = new ArrayList<>();
	    for (CartItem cartItem : productsInCart) {
	        Integer remainingQuantity = cartItem.getCartProduct().getQuantity() - cartItem.getCartItemQuantity();
	        if (remainingQuantity < 0 || cartItem.getCartProduct().getStatus() == ProductStatus.OUTOFSTOCK) {
	            CartDTO cartdto = new CartDTO();
	            cartdto.setProductId(cartItem.getCartProduct().getProductId());
	            cartservicei.removeProductFromCart(cartdto, token);
	            throw new OrderException("Product " + cartItem.getCartProduct().getProductName() + " OUT OF STOCK");
	        }

	        cartItem.getCartProduct().setQuantity(remainingQuantity);
	        if (cartItem.getCartProduct().getQuantity() == 0) {
	            cartItem.getCartProduct().setStatus(ProductStatus.OUTOFSTOCK);
	        }

	        productsInOrder.add(cartItem);
	    }

	    newOrder.setOrdercartItems(productsInOrder);
	    newOrder.setTotal(customer.getCustomerCart().getCartTotal());

	    if (userStoredCard.getCardNumber().equals(userProvidedCard.getCardNumber()) &&
	        userStoredCard.getCardValidity().equals(userProvidedCard.getCardValidity()) &&
	        userStoredCard.getCardCVV().equals(userProvidedCard.getCardCVV())) {

	        newOrder.setCardNumber(userProvidedCard.getCardNumber());
	        newOrder.setAddress(customer.getAddress().get(odto.getAddressType()));
	        newOrder.setDate(LocalDate.now());
	        newOrder.setOrderStatus(OrderStatusValues.SUCCESS);

	    } else {
	        newOrder.setCardNumber(null);
	        newOrder.setAddress(customer.getAddress().get(odto.getAddressType()));
	        newOrder.setDate(LocalDate.now());
	        newOrder.setOrderStatus(OrderStatusValues.PENDING);
	    }

	    Order savedOrder = orderRepository.save(newOrder);
	    cartservicei.clearCart(token);

	    return savedOrder;
	}


	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	public Order getOrderByOrderId(Integer OrderId) throws OrderException {
		return orderRepository.findById(OrderId).orElseThrow(()-> new OrderException("No order exists with given OrderId "+ OrderId));
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	public List<Order> getAllOrders() throws OrderException {
		// TODO Auto-generated method stub
		List<Order> orders = orderRepository.findAll();
		if(orders.size()>0)
			return orders;
		else
			throw new OrderException("No Orders exists on your account");
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	public Order cancelOrderByOrderId(Integer OrderId,String token) throws OrderException {
		Order order= orderRepository.findById(OrderId).orElseThrow(()->new OrderException("No order exists with given OrderId "+ OrderId));
		if(order.getCustomer().getCustomerId()==customerService.getLoggedInCustomerDetails(token).getCustomerId()) {
			if(order.getOrderStatus()==OrderStatusValues.PENDING) {
				order.setOrderStatus(OrderStatusValues.CANCELLED);
				orderRepository.save(order);
				return order;
			}
			else if(order.getOrderStatus()==OrderStatusValues.SUCCESS) {
				order.setOrderStatus(OrderStatusValues.CANCELLED);
				List<CartItem> cartItemsList= order.getOrdercartItems();
				
				for(CartItem cartItem : cartItemsList ) {
					Integer addedQuantity = cartItem.getCartProduct().getQuantity()+cartItem.getCartItemQuantity();
					cartItem.getCartProduct().setQuantity(addedQuantity);
					if(cartItem.getCartProduct().getStatus() == ProductStatus.OUTOFSTOCK) {
						cartItem.getCartProduct().setStatus(ProductStatus.AVAILABLE);
					}
				}
				
				orderRepository.save(order);
				return order;
			}
			else {
				throw new OrderException("Order was already cancelled");
			}
		}
		else {
			throw new LoginException("Invalid session token for customer"+"Kindly Login");
		}

		
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	public Order updateOrderByOrder(OrderDTO orderdto, Integer OrderId,String token) throws OrderException,LoginException {
		Order existingOrder= orderRepository.findById(OrderId).orElseThrow(()->new OrderException("No order exists with given OrderId "+ OrderId));
		if(existingOrder.getCustomer().getCustomerId()==customerService.getLoggedInCustomerDetails(token).getCustomerId()) {
			Customer loggedInCustomer = customerService.getLoggedInCustomerDetails(token);
			String usersCardNumber= loggedInCustomer.getCreditCard().getCardNumber();
			String userGivenCardNumber= orderdto.getCardNumber().getCardNumber();
			if((usersCardNumber.equals(userGivenCardNumber)) 
					&& (orderdto.getCardNumber().getCardValidity().equals(loggedInCustomer.getCreditCard().getCardValidity())
							&& (orderdto.getCardNumber().getCardCVV().equals(loggedInCustomer.getCreditCard().getCardCVV())))) {
				existingOrder.setCardNumber(orderdto.getCardNumber().getCardNumber());
				existingOrder.setAddress(existingOrder.getCustomer().getAddress().get(orderdto.getAddressType()));
				existingOrder.setOrderStatus(OrderStatusValues.SUCCESS);
				List<CartItem> cartItemsList= existingOrder.getOrdercartItems();
				for(CartItem cartItem : cartItemsList ) {
					Integer remainingQuantity = cartItem.getCartProduct().getQuantity()-cartItem.getCartItemQuantity();
					if(remainingQuantity < 0 || cartItem.getCartProduct().getStatus() == ProductStatus.OUTOFSTOCK) {
						CartDTO cartdto = new CartDTO();
						cartdto.setProductId(cartItem.getCartProduct().getProductId());
						cartservicei.removeProductFromCart(cartdto, token);
						throw new OrderException("Product "+ cartItem.getCartProduct().getProductName() + " OUT OF STOCK");
					}
					cartItem.getCartProduct().setQuantity(remainingQuantity);
					if(cartItem.getCartProduct().getQuantity()==0) {
						cartItem.getCartProduct().setStatus(ProductStatus.OUTOFSTOCK);
					}
				}
				return orderRepository.save(existingOrder);
			}
			else {
				throw new OrderException("Incorrect Card Number Again" + usersCardNumber + userGivenCardNumber);
			}
			
		}
		else {
			throw new LoginException("Invalid session token for customer"+"Kindly Login");
		}
		
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	public List<Order> getAllOrdersByDate(LocalDate date) throws OrderException {
		List<Order> listOfOrdersOntheDay= orderRepository.findByDate(date);
		return listOfOrdersOntheDay;
	}
	
	@PreAuthorize("hasAuthority('SELLER')")
	@Override
	public Customer getCustomerByOrderid(Integer orderId) throws OrderException {
		Optional<Order> order= orderRepository.findById(orderId);
		if(order.isPresent()) {
			Order existingorder= order.get();
			return orderRepository.getCustomerByOrderid(existingorder.getCustomer().getCustomerId());
		}
		else
			throw new OrderException("No Order exists with orderId "+orderId);
	}
	
}
