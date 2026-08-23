package com.tejas.mapper;

import com.tejas.model.Order;
import com.tejas.payload.dto.OrderDto;

import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDto toDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .branchId(order.getBranch() != null ? order.getBranch().getId() : null)
                .cashier(UserMapper.toDto(order.getCashier()))
                .customer(order.getCustomer())
                .paymentType(order.getPaymentType())
                .items(order.getItems().stream().map(OrderItemMapper::toDto).collect(Collectors.toList()))
                .build();
    }
}
