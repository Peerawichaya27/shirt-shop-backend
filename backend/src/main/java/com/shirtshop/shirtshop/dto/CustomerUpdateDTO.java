package com.shirtshop.shirtshop.dto;

import java.util.HashMap;
import java.util.Map;

import com.shirtshop.shirtshop.entity.Address;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class CustomerUpdateDTO {
	
	@Pattern(regexp = "[A-Za-z.\\s]+", message = "Enter valid characters in first name")
	private String firstName;
	
	@Pattern(regexp = "[A-Za-z.\\s]+", message = "Enter valid characters in last name")
	private String lastName;
	
	
	@NotNull(message = "Please enter username")
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{7,29}$", message = "Enter valid characters in username")
	private String username;
	
	@Pattern(regexp = "[A-Za-z0-9!@#$%^&*_]{8,15}", message = "Password must be 8-15 characters in length and can include A-Z, a-z, 0-9, or special characters !@#$%^&*_")
	private String password;
	
	private String Role;
	
	private Map<String, Address> address = new HashMap<>();
	
  

	    
	
}
