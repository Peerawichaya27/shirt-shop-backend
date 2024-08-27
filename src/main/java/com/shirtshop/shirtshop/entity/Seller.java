package com.shirtshop.shirtshop.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seller implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer sellerId;
	
	@NotNull(message="Please enter the first name")
	//@Pattern(regexp="[A-Za-z\\s]+", message="First Name should contains alphabets only")
	private String firstName;
	
	@NotNull(message="Please enter the last name")
	//@Pattern(regexp="[A-Za-z\\s]+", message="First Name should contains alphabets only")
	private String lastName;
	
	@NotNull(message = "role cannot be NULL")
	private String role;
	
	@NotNull(message = "Please enter username")
	@Column(unique = true)
	//@Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{7,29}$", message = "Enter valid characters in username")
	private String username;
	
    //@Pattern(regexp="[A-Za-z0-9!@#$%^&*_]{8,15}", message="Please Enter a valid Password")
	private String password;
	

	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},orphanRemoval = true)
    @JsonIgnore
    private List<Product> product = new ArrayList<>();
	

    public String getPassword() {
        return password;
    }
    
    public Seller setUsername(String username) {
        this.username = username;
        return this;
    }
    
    public Seller setFirstName(String firstName) {
    	this.firstName = firstName;
    	return this;
    }
    
    public Seller setLastName(String lastName) {
    	this.lastName = lastName;
    	return this;
    }
    
    public Seller setRole(String role) {
    	this.role = role;
    	return this;
    }
    
    public Seller setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String getUsername() {
        return username;
    }
	
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
}