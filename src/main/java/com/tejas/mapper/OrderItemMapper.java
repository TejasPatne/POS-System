package com.tejas.mapper;

import com.tejas.model.Order;
import com.tejas.model.OrderItem;
import com.tejas.payload.dto.OrderItemDto;
import jakarta.persistence.ManyToOne;

public class OrderItemMapper {
    public static OrderItemDto toDto(OrderItem item) {
        if (item == null) return null;
        return OrderItemDto.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .product(ProductMapper.toDto(item.getProduct()))
                .build();
    }
}
