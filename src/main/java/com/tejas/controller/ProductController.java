package com.tejas.controller;

import com.tejas.model.User;
import com.tejas.payload.dto.ProductDto;
import com.tejas.payload.response.ApiResponse;
import com.tejas.service.ProductService;
import com.tejas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto,
                                             @RequestHeader("Authorization") String jwt) {
        User user = userService.getUserFromJwtToken(jwt);
        return new ResponseEntity<>(productService.createProduct(productDto, user), HttpStatus.CREATED);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductDto>> getByStoreId(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String jwt) {
        return new ResponseEntity<>(productService.getProductsByStoreId(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id,
                                             @RequestBody ProductDto productDto,
                                             @RequestHeader("Authorization") String jwt) {
        User user = userService.getUserFromJwtToken(jwt);
        return new ResponseEntity<>(productService.updateProduct(id, productDto, user), HttpStatus.OK);
    }

    @GetMapping("/store/{storeId}/search")
    public ResponseEntity<List<ProductDto>> searchByKeyword(@PathVariable Long id,
                                                            @RequestParam String keyword) {
        return new ResponseEntity<>(productService.searchByKeyword(id, keyword), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> delete(@PathVariable Long id,
                                              @RequestHeader("Authorization") String jwt) {
        User user = userService.getUserFromJwtToken(jwt);
        productService.deleteProduct(id, user);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Hakai! Product erased from existence");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
