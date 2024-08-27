package com.shirtshop.shirtshop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.shirtshop.shirtshop.dto.UserDTO;
import com.shirtshop.shirtshop.entity.Seller;
import com.shirtshop.shirtshop.services.SellerService;

import jakarta.validation.Valid;

@RestController
public class SellerController {
	
	@Autowired
	private SellerService sellerService;
	
	@PreAuthorize("hasAuthority('SELLER')")
	@GetMapping("/sellers")
	public ResponseEntity<List<Seller>> getAllSellerHandler(@RequestHeader("Authorization") String token){
		token = token.substring("Bearer ".length());
		return new ResponseEntity<>(sellerService.getAllSellers(token), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('SELLER')")
	@GetMapping("/sellers/current")
	public ResponseEntity<Seller> getLoggedInSellerDetailsHandler(@RequestHeader("Authorization") String token){
		token = token.substring("Bearer ".length());
		return ResponseEntity.ok(sellerService.getLoggedInSellerDetails(token));
	}
	
	@PreAuthorize("hasAuthority('SELLER')")
	@PutMapping("/sellers/update/password")
	public ResponseEntity<Seller> updateSellerPasswordHandler(@Valid @RequestBody UserDTO customerDto, @RequestHeader("Authorization") String token){	
		token = token.substring("Bearer ".length());
		return new ResponseEntity<>(sellerService.updateSellerPassword(customerDto, token), HttpStatus.ACCEPTED);
	}
}
