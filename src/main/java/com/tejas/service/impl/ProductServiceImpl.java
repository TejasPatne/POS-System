package com.tejas.service.impl;

import com.tejas.mapper.ProductMapper;
import com.tejas.model.Category;
import com.tejas.model.Product;
import com.tejas.model.Store;
import com.tejas.model.User;
import com.tejas.payload.dto.ProductDto;
import com.tejas.repository.CategoryRepository;
import com.tejas.repository.ProductRepository;
import com.tejas.repository.StoreRepository;
import com.tejas.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto, User user) {
        Store store = storeRepository.findById(productDto.getStoreId())
                .orElseThrow(() -> new RuntimeException("I searched the entire universe. No store."));
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        Product product = ProductMapper.toEntity(productDto, store, category);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto, User user) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scouter says: Product not detected."));

        product.setName(product.getName());
        product.setDescription(product.getDescription());
        product.setSku(product.getSku());
        product.setImage(product.getImage());
        product.setMrp(product.getMrp());
        product.setSellingPrice(product.getSellingPrice());
        product.setBrand(product.getBrand());
        product.setUpdatedAt(LocalDateTime.now());

        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        Product savedProduct = productRepository.save(product);
        return ProductMapper.toDto(savedProduct);
    }

    @Override
    public void deleteProduct(Long id, User user) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scouter says: Product not detected."));
        productRepository.delete(product);
    }

    @Override
    public List<ProductDto> getProductsByStoreId(Long storeId) {
        List<Product> products = productRepository.findByStoreId(storeId);
        return products.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> searchByKeyword(Long storeId, String keyword) {
        List<Product> products = productRepository.searchByKeyword(storeId, keyword);
        return products.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }
}
