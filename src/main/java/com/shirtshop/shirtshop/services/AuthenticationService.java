package com.shirtshop.shirtshop.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shirtshop.shirtshop.dto.CustomerUpdateDTO;
import com.shirtshop.shirtshop.dto.SellerUpdateDTO;
import com.shirtshop.shirtshop.dto.UserDTO;
import com.shirtshop.shirtshop.entity.Customer;
import com.shirtshop.shirtshop.entity.Seller;
import com.shirtshop.shirtshop.entity.Users;
import com.shirtshop.shirtshop.repositories.CustomerRepository;
import com.shirtshop.shirtshop.repositories.SellerRepository;
import com.shirtshop.shirtshop.repositories.UserRepository;

@Service
public class AuthenticationService {
    private final CustomerRepository customerRepository;
    
    private final SellerRepository sellerRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;
    
    private final JwtService jwtService;
    
    @Autowired
    private UserRepository userRepository;
    

    public AuthenticationService(
        CustomerRepository customerRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        SellerRepository sellerRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.sellerRepository = sellerRepository;
    }

    public Customer CustomerSignup(CustomerUpdateDTO input) {
    	
    	
    	Users user = new Users()
    			.setFirstName(input.getFirstName())
    			.setLastName(input.getLastName())
    			.setRole("Customer")
    			.setUsername(input.getUsername())
    			.setPassword(passwordEncoder.encode(input.getPassword()));
    	userRepository.save(user);
    	Customer customers = new Customer()
    			.setFirstName(input.getFirstName())
    			.setLastName(input.getLastName())
    			.setRole("Customer")
    			.setUsername(input.getUsername())
    			.setPassword(passwordEncoder.encode(input.getPassword()));
        return customerRepository.save(customers);
    }
    
    public Seller SellerSignup(SellerUpdateDTO input) {
    	Users user = new Users()
    			.setFirstName(input.getFirstName())
    			.setLastName(input.getLastName())
    			.setRole("Seller")
    			.setUsername(input.getUsername())
    			.setPassword(passwordEncoder.encode(input.getPassword()));
    	userRepository.save(user);
       	Seller sellers = new Seller()
    			.setFirstName(input.getFirstName())
    			.setLastName(input.getLastName())
    			.setRole("Seller")
    			.setUsername(input.getUsername())
    			.setPassword(passwordEncoder.encode(input.getPassword()));
        return sellerRepository.save(sellers);
    }

    public Users userAuthenticate(UserDTO input) {
    			authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );
        
        return userRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }
    
    
}