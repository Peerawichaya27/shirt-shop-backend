package com.shirtshop.shirtshop.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer customerId;
	
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
	
	
	private LocalDateTime createdOn;
	
	@Embedded
	private CreditCard creditCard;
	
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "customer_address_mapping",
				joinColumns = {
						@JoinColumn(name = "customer_id", referencedColumnName = "customerId")
				},
				inverseJoinColumns = {
						@JoinColumn(name = "address_id", referencedColumnName = "addressId")
				})
	private Map<String, Address> address = new HashMap<>();
	
	

	
	
//	Establishing Customer - Order relationship
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
	private List<Order> orders = new ArrayList<>();
	
	
	
//	Establishing Customer - Cart relationship
//	
	@OneToOne(cascade = CascadeType.ALL)
	private Cart customerCart;

	

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<>();
	    auths.add(new SimpleGrantedAuthority(getRole().toUpperCase()));
        return auths;
    }

    public String getPassword() {
        return password;
    }
    
    public Customer setUsername(String username) {
        this.username = username;
        return this;
    }
    
    public Customer setFirstName(String firstName) {
    	this.firstName = firstName;
    	return this;
    }
    
    public Customer setLastName(String lastName) {
    	this.lastName = lastName;
    	return this;
    }
    
    public Customer setRole(String role) {
    	this.role = role;
    	return this;
    }
    
    public Customer setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String getUsername() {
        return username;
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
