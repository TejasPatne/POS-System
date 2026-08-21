package com.tejas.payload.dto;

import com.tejas.model.Store;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String sku;
    private String description;
    private Double mrp;
    private Double sellingPrice;
    private String brand;
    private String image;
    private CategoryDto category;
    private Long categoryId;
    private Long storeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
