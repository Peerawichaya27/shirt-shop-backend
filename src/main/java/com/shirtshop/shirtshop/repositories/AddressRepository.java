package com.shirtshop.shirtshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shirtshop.shirtshop.entity.Address;


@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{

}
