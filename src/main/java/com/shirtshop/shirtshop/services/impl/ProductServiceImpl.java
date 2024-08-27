package com.shirtshop.shirtshop.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.shirtshop.shirtshop.dto.ProductDTO;
import com.shirtshop.shirtshop.entity.CategoryEnum;
import com.shirtshop.shirtshop.entity.Product;
import com.shirtshop.shirtshop.entity.ProductStatus;
import com.shirtshop.shirtshop.entity.Seller;
import com.shirtshop.shirtshop.exception.CategoryNotFoundException;
import com.shirtshop.shirtshop.exception.ProductNotFoundException;
import com.shirtshop.shirtshop.repositories.ProductRepository;
import com.shirtshop.shirtshop.repositories.SellerRepository;
import com.shirtshop.shirtshop.services.ProductService;
import com.shirtshop.shirtshop.services.SellerService;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SellerService sellerService;

	@Autowired
	private SellerRepository sellerRepository;
	
	@PreAuthorize("hasAuthority('SELLER')")
	@Override
	public Product addProductToCatalog(String token, Product product) {
		Product prod = null;
		Seller Existingseller = sellerService.getLoggedInSellerDetails(token);
		product.setSeller(Existingseller);
		prod = productRepository.save(product);
		Existingseller.getProduct().add(product);
		sellerRepository.save(Existingseller);
		return prod;
	}
	
	@Override
	public Product getProductFromCatalogById(Integer id) throws ProductNotFoundException {
		Optional<Product> product = productRepository.findById(id);
		if (product.isPresent()) {
			return product.get();
		}
		else
			throw new ProductNotFoundException("Product not found with given id");
	}
	
	@PreAuthorize("hasAuthority('SELLER')")
	@Override
	public String deleteProductFromCatalog(Integer id) throws ProductNotFoundException {
	    Optional<Product> product = productRepository.findById(id);
	    if (product.isPresent()) {
	        Product prod = product.get();
	        Seller seller = prod.getSeller();
	        if (seller != null) {
	            seller.getProduct().remove(prod);
	            sellerRepository.save(seller);
	        }
	        productRepository.delete(prod);
	        return "Product deleted from catalog";
	    } else {
	        throw new ProductNotFoundException("Product not found with given id");
	    }
	}
	
	@PreAuthorize("hasAuthority('SELLER')")
	@Override
	public Product updateProductIncatalog(Product prod) throws ProductNotFoundException {
		Optional<Product> product = productRepository.findById(prod.getProductId());
		if (product.isPresent()) {
			product.get();
			Product prod1 = productRepository.save(prod);
			return prod1;
		} else
			throw new ProductNotFoundException("Product not found with given id");
	}

	@Override
	public List<Product> getAllProductsIncatalog() {
		List<Product> list = productRepository.findAll();
		if (list.size() > 0) {
			return list;
		} else
			throw new ProductNotFoundException("No products in catalog");
	}

	@Override
	public List<ProductDTO> getProductsOfCategory(CategoryEnum catenum) {
		List<ProductDTO> list = productRepository.getAllProductsInACategory(catenum);
		if (list.size() > 0) {
			return list;
		} else
			throw new CategoryNotFoundException("No products found with category:" + catenum);
	}

	@Override
	public List<ProductDTO> getProductsOfStatus(ProductStatus status) {
		List<ProductDTO> list = productRepository.getProductsWithStatus(status);
		if (list.size() > 0) {
			return list;
		} else
			throw new ProductNotFoundException("No products found with given status:" + status);
	}

	@PreAuthorize("hasAuthority('SELLER')")
	@Override
	public Product updateProductQuantityWithId(Integer id,ProductDTO prodDto) {
		 Product prod = null;
		 Optional<Product> product = productRepository.findById(id);
		 if(product!=null) {
			 prod = product.get();
			 prod.setQuantity(prod.getQuantity()+prodDto.getQuantity());
			 if(prod.getQuantity()>0) {
				 prod.setStatus(ProductStatus.AVAILABLE);
			 }
			 productRepository.save(prod);
		 }
		 else
			 throw new ProductNotFoundException("No product found with this Id");
		return prod;
	}


	@Override
	public List<ProductDTO> getAllProductsOfSeller(Integer id) {
		List<ProductDTO> list = productRepository.getProductsOfASeller(id);
		if(list.size()>0) {
			return list;
		}
		else {
			throw new ProductNotFoundException("No products with SellerId: "+id);
		}
	}
}
