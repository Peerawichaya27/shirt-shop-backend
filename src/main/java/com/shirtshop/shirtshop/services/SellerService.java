package com.shirtshop.shirtshop.services;

import java.util.List;

import com.shirtshop.shirtshop.dto.UserDTO;
import com.shirtshop.shirtshop.entity.Seller;
import com.shirtshop.shirtshop.exception.SellerException;

public interface SellerService {

	public List<Seller> getAllSellers(String token) throws SellerException;

	public Seller getLoggedInSellerDetails(String token);

	public Seller updateSellerPassword(UserDTO input, String token);

}
