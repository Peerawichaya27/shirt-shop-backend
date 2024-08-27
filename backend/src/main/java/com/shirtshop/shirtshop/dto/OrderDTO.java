package com.shirtshop.shirtshop.dto;

import com.shirtshop.shirtshop.entity.CreditCard;

import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class OrderDTO {
	
	@NotNull
	private CreditCard cardNumber;
	@NotNull
	private String addressType;
	
}
