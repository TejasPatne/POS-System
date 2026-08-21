package com.tejas.service;

import com.tejas.model.Category;
import com.tejas.model.Product;
import com.tejas.model.User;
import com.tejas.payload.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto, User user);
    ProductDto updateProduct(Long id, ProductDto productDto, User user);
    void deleteProduct(Long id, User user);
    List<ProductDto> getProductsByStoreId(Long storeId);
    List<ProductDto> searchByKeyword(Long storeId, String keyword);
}
