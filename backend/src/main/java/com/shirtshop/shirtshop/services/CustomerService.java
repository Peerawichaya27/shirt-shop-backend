package com.shirtshop.shirtshop.services;

import java.util.List;

import com.shirtshop.shirtshop.dto.UserDTO;
import com.shirtshop.shirtshop.entity.Address;
import com.shirtshop.shirtshop.entity.CreditCard;
import com.shirtshop.shirtshop.entity.Customer;
import com.shirtshop.shirtshop.entity.Order;
import com.shirtshop.shirtshop.exception.CustomerException;
import com.shirtshop.shirtshop.exception.CustomerNotFoundException;


public interface CustomerService {
	
	
	public Customer getLoggedInCustomerDetails(String token);
	
	public Customer updateCustomerPassword(UserDTO input, String token);

	public List<Customer> getAllCustomers(String token) throws CustomerNotFoundException;

	public Customer updateAddress(Address address, String type, String token) throws CustomerException;

	public Customer deleteAddress(String type, String token) throws CustomerException, CustomerNotFoundException;

	public Customer updateCreditCardDetails(String token, CreditCard card) throws CustomerException;

	public List<Order> getCustomerOrders(String token) throws CustomerException;

}
