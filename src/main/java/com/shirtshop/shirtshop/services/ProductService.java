package com.shirtshop.shirtshop.services;

import java.util.List;

import com.shirtshop.shirtshop.dto.ProductDTO;
import com.shirtshop.shirtshop.entity.CategoryEnum;
import com.shirtshop.shirtshop.entity.Product;
import com.shirtshop.shirtshop.entity.ProductStatus;
import com.shirtshop.shirtshop.exception.ProductNotFoundException;


public interface ProductService {

	public Product addProductToCatalog(String token, Product product);

	public Product getProductFromCatalogById(Integer id) throws ProductNotFoundException;

	public String deleteProductFromCatalog(Integer id) throws ProductNotFoundException;

	public Product updateProductIncatalog(Product prod) throws ProductNotFoundException;

	public List<Product> getAllProductsIncatalog();

	public List<ProductDTO> getProductsOfCategory(CategoryEnum catenum);

	public List<ProductDTO> getProductsOfStatus(ProductStatus status);

	public Product updateProductQuantityWithId(Integer id, ProductDTO prodDto);

	public List<ProductDTO> getAllProductsOfSeller(Integer id);

}
