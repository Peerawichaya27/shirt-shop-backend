package com.shirtshop.shirtshop.services;

import java.time.LocalDate;
import java.util.List;

import com.shirtshop.shirtshop.dto.OrderDTO;
import com.shirtshop.shirtshop.entity.Customer;
import com.shirtshop.shirtshop.entity.Order;
import com.shirtshop.shirtshop.exception.LoginException;
import com.shirtshop.shirtshop.exception.OrderException;

public interface OrderService {

	public Order saveOrder(OrderDTO odto, String token) throws LoginException, OrderException;

	public Order getOrderByOrderId(Integer OrderId) throws OrderException;

	public List<Order> getAllOrders() throws OrderException;

	public Order cancelOrderByOrderId(Integer OrderId, String token) throws OrderException;

	public Order updateOrderByOrder(OrderDTO orderdto, Integer OrderId, String token) throws OrderException, LoginException;

	public List<Order> getAllOrdersByDate(LocalDate date) throws OrderException;

	public Customer getCustomerByOrderid(Integer orderId) throws OrderException;

}
