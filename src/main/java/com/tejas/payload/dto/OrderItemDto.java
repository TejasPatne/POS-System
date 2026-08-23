package com.tejas.payload.dto;

import com.tejas.model.Order;
import com.tejas.model.Product;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDto {
    private Long id;
    private Integer quantity;
    private Double price;
    private ProductDto product;
    private Long productId;
    private Long orderId;
}
