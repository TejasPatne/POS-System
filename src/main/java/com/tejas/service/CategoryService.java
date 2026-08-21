package com.tejas.service;

import com.tejas.payload.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    List<CategoryDto> getCategoriesByStore(Long storeId);
    CategoryDto updateCategory(Long id, CategoryDto categoryDto);
    void deleteCategory(Long id);
}
