package com.tejas.controller;

import com.tejas.payload.dto.CategoryDto;
import com.tejas.payload.response.ApiResponse;
import com.tejas.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto categoryDto) {
        CategoryDto categoryResult = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(categoryResult, HttpStatus.CREATED);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<CategoryDto>> getByStoreId(@PathVariable Long storeId) {
        List<CategoryDto> categoryListResult = categoryService.getCategoriesByStore(storeId);
        return new ResponseEntity<>(categoryListResult, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@RequestBody CategoryDto categoryDto,
                                              @PathVariable Long id) {
        CategoryDto categoryResult = categoryService.updateCategory(id, categoryDto);
        return new ResponseEntity<>(categoryResult, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Category deleted. One less problem for the universe.");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
