package com.shirtshop.shirtshop.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shirtshop.shirtshop.dto.UserDTO;
import com.shirtshop.shirtshop.entity.Seller;
import com.shirtshop.shirtshop.entity.Users;
import com.shirtshop.shirtshop.exception.CustomerException;
import com.shirtshop.shirtshop.exception.CustomerNotFoundException;
import com.shirtshop.shirtshop.exception.SellerException;
import com.shirtshop.shirtshop.repositories.SellerRepository;
import com.shirtshop.shirtshop.repositories.UserRepository;
import com.shirtshop.shirtshop.services.JwtService;
import com.shirtshop.shirtshop.services.SellerService;

@Service
public class SellerServiceImpl implements SellerService {
	
	@Autowired
	private SellerRepository sellerRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;
	
	@PreAuthorize("hasAuthority('SELLER')")
    @Override
	public List<Seller> getAllSellers(String token) throws SellerException {
		List<Seller> seller = sellerRepository.findAll();	
		if(seller.size() == 0)
			throw new CustomerNotFoundException("No Seller Found !");	
		return seller;
	}
	
	@PreAuthorize("hasAuthority('SELLER')")
    @Override
	public Seller getLoggedInSellerDetails(String token){  	
    	String user = jwtService.extractUsername(token);
		Optional<Seller> seller = sellerRepository.findByUsername(user);
		return seller.get();
	}
	
	@PreAuthorize("hasAuthority('SELLER')")
	@Override
	public Seller updateSellerPassword(UserDTO input, String token) {
		 String userFromtoken = jwtService.extractUsername(token);
         Optional<Seller> seller = sellerRepository.findByUsername(userFromtoken);
         Seller existingSeller = seller.get();
         if(input.getUsername().equals(existingSeller.getUsername()) == false) {
 			throw new CustomerException("Verification error. Username does not match");
 			}
         existingSeller.setPassword(passwordEncoder.encode(input.getPassword()));
 		 Optional<Users> user = userRepository.findByUsername(userFromtoken);
 		 Users users = user.get();
 		 users.setPassword(passwordEncoder.encode(input.getPassword()));
 		 userRepository.save(users);
         return sellerRepository.save(existingSeller);
     }
	
}
