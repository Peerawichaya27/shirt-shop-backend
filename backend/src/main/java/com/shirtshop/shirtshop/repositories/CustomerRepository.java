package com.shirtshop.shirtshop.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shirtshop.shirtshop.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	    Optional<Customer> findByUsername(String username);
	    Optional<Customer> findById(Integer customerId);
	}
