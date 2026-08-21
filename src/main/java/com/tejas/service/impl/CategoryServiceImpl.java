package com.tejas.service.impl;

import com.tejas.domain.UserRole;
import com.tejas.exceptions.UserException;
import com.tejas.mapper.CategoryMapper;
import com.tejas.model.Category;
import com.tejas.model.Store;
import com.tejas.model.User;
import com.tejas.payload.dto.CategoryDto;
import com.tejas.repository.CategoryRepository;
import com.tejas.repository.StoreRepository;
import com.tejas.service.CategoryService;
import com.tejas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final StoreRepository storeRepository;
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        User user = userService.getCurrentUser();
        Store store = storeRepository.findById(categoryDto.getStoreId()).orElseThrow(() -> new RuntimeException("Store not found"));
        checkAuthority(user, store);

        Category category = Category.builder().name(categoryDto.getName()).store(store).build();
        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> getCategoriesByStore(Long storeId) {
        List<Category> categories = categoryRepository.findByStoreId(storeId);
        return categories.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        User user = userService.getCurrentUser();
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("category does not exist"));
        checkAuthority(user, category.getStore());

        category.setName(categoryDto.getName());
        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        User user = userService.getCurrentUser();
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("category does not exist"));
        checkAuthority(user, category.getStore());

        categoryRepository.delete(category);
    }

    private void checkAuthority(User user, Store store) {
        boolean isStoreAdmin = user.getRole().equals(UserRole.ROLE_STORE_ADMIN);
        boolean isStoreManager = user.getRole().equals(UserRole.ROLE_STORE_MANAGER);
        boolean isSameStore = user.equals(store.getStoreAdmin());

        if (!(isStoreAdmin && isSameStore) && !isStoreManager) {
            throw new UserException("You don't have permission to manage this category");
        }
    }
}
