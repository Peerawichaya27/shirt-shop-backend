package com.shirtshop.shirtshop.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shirtshop.shirtshop.dto.ProductDTO;
import com.shirtshop.shirtshop.entity.CategoryEnum;
import com.shirtshop.shirtshop.entity.Product;
import com.shirtshop.shirtshop.entity.ProductStatus;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	
	@Query("select new com.shirtshop.shirtshop.dto.ProductDTO(p.productName,p.price,p.quantity) "
			+ "from Product p where p.category=:catenum")
	public List<ProductDTO> getAllProductsInACategory(@Param("catenum") CategoryEnum catenum);
	
	
	@Query("select new com.shirtshop.shirtshop.dto.ProductDTO(p.productName,p.price,p.quantity) "
			+ "from Product p where p.status=:status")
	public List<ProductDTO> getProductsWithStatus(@Param("status") ProductStatus status);
	
	@Query("select new com.shirtshop.shirtshop.dto.ProductDTO(p.productName,p.price,p.quantity) "
			+ "from Product p where p.seller.sellerId=:id")
	public List<ProductDTO> getProductsOfASeller(@Param("id") Integer id);
	

}
