package com.tejas.mapper;

import com.tejas.model.Category;
import com.tejas.payload.dto.CategoryDto;

public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .storeId(category.getStore()!=null ? category.getStore().getId(): null)
                .build();
    }
}
