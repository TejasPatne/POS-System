package com.tejas.payload.dto;

import com.tejas.domain.PaymentType;
import com.tejas.model.Branch;
import com.tejas.model.Customer;
import com.tejas.model.OrderItem;
import com.tejas.model.User;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDto {
    private Long id;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private Long branchId;
    private BranchDto branch;
    private UserDto cashier;
    private Long customerId;
    private Customer customer;
    private PaymentType paymentType;
    private List<OrderItemDto> items;
}
