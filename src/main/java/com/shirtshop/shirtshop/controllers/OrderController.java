package com.shirtshop.shirtshop.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shirtshop.shirtshop.dto.OrderDTO;
import com.shirtshop.shirtshop.entity.Customer;
import com.shirtshop.shirtshop.entity.Order;
import com.shirtshop.shirtshop.services.OrderService;

import jakarta.validation.Valid;

@RestController
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@PostMapping("/order/add")
	public ResponseEntity<Order> addTheNewOrder(@Valid @RequestBody OrderDTO odto,@RequestHeader("Authorization") String token){	
		token = token.substring("Bearer ".length());
		Order savedorder = orderService.saveOrder(odto,token);
		return new ResponseEntity<Order>(savedorder,HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@GetMapping("/orders")
	public List<Order> getAllOrders(){
		List<Order> listOfAllOrders = orderService.getAllOrders();
		return listOfAllOrders;	
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@GetMapping("/orders/{orderId}")
	public Order getOrdersByOrderId(@PathVariable("orderId") Integer orderId) {	
		return orderService.getOrderByOrderId(orderId);		
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@DeleteMapping("/orders/{orderId}")
	public Order cancelTheOrderByOrderId(@PathVariable("orderId") Integer orderId,@RequestHeader("Authorization") String token){
		token = token.substring("Bearer ".length());
		return orderService.cancelOrderByOrderId(orderId,token);
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@PutMapping("/orders/{orderId}")
	public ResponseEntity<Order> updateOrderByOrder(@Valid @RequestBody OrderDTO orderdto, @PathVariable("orderId") Integer orderId,@RequestHeader("Authorization") String token){
		token = token.substring("Bearer ".length());
		Order updatedOrder= orderService.updateOrderByOrder(orderdto,orderId,token);
		return new ResponseEntity<Order>(updatedOrder,HttpStatus.ACCEPTED);
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@GetMapping("/orders/by/date")
	public List<Order> getOrdersByDate(@RequestParam("date") String date){
		DateTimeFormatter dtf=DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate ld=LocalDate.parse(date,dtf);
		return orderService.getAllOrdersByDate(ld);
	}
	
	@PreAuthorize("hasAuthority('SELLER')")
	@GetMapping("/customer/{orderId}")
	public Customer getCustomerDetailsByOrderId(@PathVariable("orderId") Integer orderId) {
		return orderService.getCustomerByOrderid(orderId);
	}
}
