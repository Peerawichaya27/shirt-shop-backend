package com.shirtshop.shirtshop.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shirtshop.shirtshop.dto.UserDTO;
import com.shirtshop.shirtshop.entity.Address;
import com.shirtshop.shirtshop.entity.CreditCard;
import com.shirtshop.shirtshop.entity.Customer;
import com.shirtshop.shirtshop.entity.Order;
import com.shirtshop.shirtshop.entity.Users;
import com.shirtshop.shirtshop.exception.CustomerException;
import com.shirtshop.shirtshop.exception.CustomerNotFoundException;
import com.shirtshop.shirtshop.repositories.CustomerRepository;
import com.shirtshop.shirtshop.repositories.UserRepository;
import com.shirtshop.shirtshop.services.CustomerService;
import com.shirtshop.shirtshop.services.JwtService;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
    private CustomerRepository customerRepository;
	@Autowired
    private PasswordEncoder passwordEncoder;
	@Autowired
    private JwtService jwtService;
	@Autowired
	private UserRepository userRepository;
	
	@PreAuthorize("hasAuthority('SELLER')")
    @Override
	public List<Customer> getAllCustomers(String token) throws CustomerNotFoundException {
		List<Customer> customers = customerRepository.findAll();	
		if(customers.size() == 0)
			throw new CustomerNotFoundException("No record exists");	
		return customers;
	}

    
    
    @Override
	public Customer getLoggedInCustomerDetails(String token){  	
    	String user = jwtService.extractUsername(token);
		Optional<Customer> customer = customerRepository.findByUsername(user);
		return customer.get();
	}
	

	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	public Customer updateCustomerPassword(UserDTO input, String token) {
		 String userFromtoken = jwtService.extractUsername(token);
         Optional<Customer> customer = customerRepository.findByUsername(userFromtoken);
         Customer existingCustomer = customer.get();
         if(input.getUsername().equals(existingCustomer.getUsername()) == false) {
 			throw new CustomerException("Verification error. Username does not match");
 			}
 		 existingCustomer.setPassword(passwordEncoder.encode(input.getPassword()));
 		 Optional<Users> user = userRepository.findByUsername(userFromtoken);
 		 Users users = user.get();
 		 users.setPassword(passwordEncoder.encode(input.getPassword()));
 		 userRepository.save(users);
         return customerRepository.save(existingCustomer);
     }
     
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	public Customer updateAddress(Address address, String type, String token) throws CustomerException {
		String user = jwtService.extractUsername(token);
        Optional<Customer> customer = customerRepository.findByUsername(user);
		Customer existingCustomer = customer.get();
		existingCustomer.getAddress().put(type, address);
		return customerRepository.save(existingCustomer);
		
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	public Customer deleteAddress(String type, String token) throws CustomerException, CustomerNotFoundException {
		String user = jwtService.extractUsername(token);
        Optional<Customer> customer = customerRepository.findByUsername(user);
		Customer existingCustomer = customer.get();
		if(existingCustomer.getAddress().containsKey(type) == false)
			throw new CustomerException("Address type does not exist");
		existingCustomer.getAddress().remove(type);
		return customerRepository.save(existingCustomer);
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	public Customer updateCreditCardDetails(String token, CreditCard card) throws CustomerException{
		String user = jwtService.extractUsername(token);
        Optional<Customer> customer = customerRepository.findByUsername(user);
		Customer existingCustomer = customer.get();
		existingCustomer.setCreditCard(card);
		return customerRepository.save(existingCustomer);
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@Override
	public List<Order> getCustomerOrders(String token) throws CustomerException {
		String user = jwtService.extractUsername(token);
        Optional<Customer> customer = customerRepository.findByUsername(user);
		Customer existingCustomer = customer.get();
		List<Order> myOrders = existingCustomer.getOrders();
		if(myOrders.size() == 0)
			throw new CustomerException("No orders found");
		return myOrders;
	}

	
}
