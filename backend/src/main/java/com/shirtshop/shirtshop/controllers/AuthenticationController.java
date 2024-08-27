package com.shirtshop.shirtshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shirtshop.shirtshop.dto.UserDTO;
import com.shirtshop.shirtshop.dto.CustomerUpdateDTO;
import com.shirtshop.shirtshop.dto.SellerUpdateDTO;
import com.shirtshop.shirtshop.entity.Customer;
import com.shirtshop.shirtshop.entity.Seller;
import com.shirtshop.shirtshop.entity.Users;
import com.shirtshop.shirtshop.response.LoginResponse;
import com.shirtshop.shirtshop.services.AuthenticationService;
import com.shirtshop.shirtshop.services.JwtService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	
	@Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping("/customer/signup")
    public ResponseEntity<Customer> register(@RequestBody CustomerUpdateDTO customerUpdateDTO) {
    	Customer registeredUser = authenticationService.CustomerSignup(customerUpdateDTO);
        return ResponseEntity.ok(registeredUser);
    }
    
    @PostMapping("/seller/signup")
    public ResponseEntity<Seller> register(@RequestBody SellerUpdateDTO sellerUpdateDTO) {
    	Seller registeredUser = authenticationService.SellerSignup(sellerUpdateDTO);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody UserDTO customerDto) {	       
        Users authenticatedUser = authenticationService.userAuthenticate(customerDto);		
		String jwtToken = jwtService.generateToken(authenticatedUser);
		LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);  
    }
    

}
