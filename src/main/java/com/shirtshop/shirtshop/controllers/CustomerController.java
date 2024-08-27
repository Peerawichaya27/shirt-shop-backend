package com.shirtshop.shirtshop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shirtshop.shirtshop.dto.UserDTO;
import com.shirtshop.shirtshop.entity.Address;
import com.shirtshop.shirtshop.entity.CreditCard;
import com.shirtshop.shirtshop.entity.Customer;
import com.shirtshop.shirtshop.entity.Order;
import com.shirtshop.shirtshop.services.CustomerService;

import jakarta.validation.Valid;

@RestController
public class CustomerController {
	
	@Autowired
    private CustomerService customerService;
    
	
	
	// Handler to Get all customer details
	@PreAuthorize("hasAuthority('SELLER')")
	@GetMapping("/customer")
	public ResponseEntity<List<Customer>> getAllCustomersHandler(@RequestHeader("Authorization") String token){
		return new ResponseEntity<>(customerService.getAllCustomers(token), HttpStatus.ACCEPTED);
	}
	
	// Handler to Get a customer details of currently logged in user - sends data as per token
	@GetMapping("/customer/current")
	public ResponseEntity<Customer> getLoggedInCustomerDetailsHandler(@RequestHeader("Authorization") String token){
		token = token.substring("Bearer ".length());
		return ResponseEntity.ok(customerService.getLoggedInCustomerDetails(token));
	}
	
	// Handler to update customer password
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@PutMapping("/customer/update/password")
	public ResponseEntity<Customer> updateCustomerPasswordHandler(@Valid @RequestBody UserDTO customerDto, @RequestHeader("Authorization") String token){	
		token = token.substring("Bearer ".length());
		return new ResponseEntity<>(customerService.updateCustomerPassword(customerDto, token), HttpStatus.ACCEPTED);
	}
	
	// Handler to Add or update new customer Address
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@PutMapping("/customer/update/address")
	public ResponseEntity<Customer> updateAddressHandler(@Valid @RequestBody Address address, @RequestParam("type") String type, @RequestHeader("Authorization") String token){
		token = token.substring("Bearer ".length());
		return new ResponseEntity<>(customerService.updateAddress(address, type, token), HttpStatus.ACCEPTED);
	}
	
	// Handler to Remove a user address
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@DeleteMapping("/customer/delete/address")
	public ResponseEntity<Customer> deleteAddressHandler(@RequestParam("type") String type, @RequestHeader("Authorization") String token){
		token = token.substring("Bearer ".length());
		return new ResponseEntity<>(customerService.deleteAddress(type, token), HttpStatus.ACCEPTED);
	}
	
	
	// Handler to update Credit card details
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@PutMapping("/customer/update/card")
	public ResponseEntity<Customer> updateCreditCardHandler(@RequestHeader("Authorization") String token, @Valid @RequestBody CreditCard newCard){
		token = token.substring("Bearer ".length());
		return new ResponseEntity<>(customerService.updateCreditCardDetails(token, newCard), HttpStatus.ACCEPTED);
	}
	
	
	// Handler to get orders details
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@GetMapping("/customer/orders")
	public ResponseEntity<List<Order>> getCustomerOrdersHandler(@RequestHeader("Authorization") String token){
		return new ResponseEntity<>(customerService.getCustomerOrders(token), HttpStatus.ACCEPTED);
	}
}
