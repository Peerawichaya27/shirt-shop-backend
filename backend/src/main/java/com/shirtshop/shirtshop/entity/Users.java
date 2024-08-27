package com.shirtshop.shirtshop.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer userId;
	
	@NotNull(message = "First Name cannot be NULL")
//	@Pattern(regexp = "[A-Za-z.\\s]+", message = "Enter valid characters in first name")
	private String firstName;
	
	@NotNull(message = "Last Name cannot be NULL")
//	@Pattern(regexp = "[A-Za-z.\\s]+", message = "Enter valid characters in last name")
	private String lastName;
	
	@NotNull(message = "role cannot be NULL")
    private String role;
	
	@NotNull(message = "Please enter username")
	@Column(unique = true)
//	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{7,29}$", message = "Enter valid characters in username")
	private String username;
	
	@NotNull(message = "Please enter the password")
//	@Pattern(regexp = "[A-Za-z0-9!@#$%^&*_]{8,15}", message = "Password must be 8-15 characters in length and can include A-Z, a-z, 0-9, or special characters !@#$%^&*_")
	private String password;
	
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<>();
	    auths.add(new SimpleGrantedAuthority(getRole().toUpperCase()));
        return auths;
    }
	
	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    public String getPassword() {
        return password;
    }
    
    public Users setUsername(String username) {
        this.username = username;
        return this;
    }
    
    public Users setFirstName(String firstName) {
    	this.firstName = firstName;
    	return this;
    }
    
    public Users setLastName(String lastName) {
    	this.lastName = lastName;
    	return this;
    }
    
    public Users setRole(String role) {
    	this.role = role;
    	return this;
    }
    
    public Users setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String getUsername() {
        return username;
    }
	
}
