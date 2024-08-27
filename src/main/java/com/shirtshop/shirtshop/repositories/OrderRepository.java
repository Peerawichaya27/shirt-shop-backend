package com.shirtshop.shirtshop.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shirtshop.shirtshop.entity.Customer;
import com.shirtshop.shirtshop.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	public List<Order> findByDate(LocalDate date);
	
	
	@Query("select c from Customer c where c.customerId = customerId")
	public Customer getCustomerByOrderid(@Param("customerId") Integer customerId);
	

	
}
