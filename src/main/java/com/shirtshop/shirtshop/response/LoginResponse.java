package com.shirtshop.shirtshop.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginResponse {
	
	    private String token;

	    private long expiresIn;
	    
	    public String getToken() {
	        return token;
	    }

	    
	    public LoginResponse setToken(String token) {
	        this.token = token;
	        return this;
	    }
	    
	    public LoginResponse setExpiresIn(long expiresIn) {
	    	this.expiresIn = expiresIn;
	    	return this;
	    }
}
