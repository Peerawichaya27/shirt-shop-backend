package com.shirtshop.shirtshop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.shirtshop.shirtshop.dto.ProductDTO;
import com.shirtshop.shirtshop.entity.CategoryEnum;
import com.shirtshop.shirtshop.entity.Product;
import com.shirtshop.shirtshop.entity.ProductStatus;
import com.shirtshop.shirtshop.services.ProductService;

import jakarta.validation.Valid;

@RestController
public class ProductController {
	
	@Autowired
	private ProductService productService;

	@PreAuthorize("hasAuthority('SELLER')")
	@PostMapping("/products")
	public ResponseEntity<Product> addProductToCatalogHandler(@RequestHeader("Authorization") String token, @Valid @RequestBody Product product) {
		token = token.substring("Bearer ".length());
		Product prod = productService.addProductToCatalog(token, product);
		return new ResponseEntity<Product>(prod, HttpStatus.ACCEPTED);
	}

	@GetMapping("/product/{id}")
	public ResponseEntity<Product> getProductFromCatalogByIdHandler(@PathVariable("id") Integer id) {
		Product prod = productService.getProductFromCatalogById(id);
		return new ResponseEntity<Product>(prod, HttpStatus.FOUND);
	}

	@PreAuthorize("hasAuthority('SELLER')")
	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProductFromCatalogHandler(@PathVariable("id") Integer id) {
		String res = productService.deleteProductFromCatalog(id);
		return new ResponseEntity<String>(res, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('SELLER')")
	@PutMapping("/products")
	public ResponseEntity<Product> updateProductInCatalogHandler(@Valid @RequestBody Product prod) {
		Product prod1 = productService.updateProductIncatalog(prod);
		return new ResponseEntity<Product>(prod1, HttpStatus.OK);

	}


	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProductsHandler() {
		List<Product> list = productService.getAllProductsIncatalog();
		return new ResponseEntity<List<Product>>(list, HttpStatus.OK);
	}
	
  
	@GetMapping("/products/seller/{id}")
	public ResponseEntity<List<ProductDTO>> getAllProductsOfSellerHandler(@PathVariable("id") Integer id) {
		List<ProductDTO> list = productService.getAllProductsOfSeller(id);
		return new ResponseEntity<List<ProductDTO>>(list, HttpStatus.OK);
	}

	@GetMapping("/products/{catenum}")
	public ResponseEntity<List<ProductDTO>> getAllProductsInCategory(@PathVariable("catenum") String catenum) {
		CategoryEnum ce = CategoryEnum.valueOf(catenum.toUpperCase());
		List<ProductDTO> list = productService.getProductsOfCategory(ce);
		return new ResponseEntity<List<ProductDTO>>(list, HttpStatus.OK);

	}

	@GetMapping("/products/status/{status}")
	public ResponseEntity<List<ProductDTO>> getProductsWithStatusHandler(@PathVariable("status") String status) {
		ProductStatus ps = ProductStatus.valueOf(status.toUpperCase());
		List<ProductDTO> list = productService.getProductsOfStatus(ps);
		return new ResponseEntity<List<ProductDTO>>(list, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('SELLER')")
	@PutMapping("/products/{id}")
	public ResponseEntity<Product> updateQuantityOfProduct(@PathVariable("id") Integer id,@RequestBody ProductDTO prodDto){
		 Product prod =   productService.updateProductQuantityWithId(id, prodDto);
		 return new ResponseEntity<Product>(prod,HttpStatus.ACCEPTED);
	}
}
