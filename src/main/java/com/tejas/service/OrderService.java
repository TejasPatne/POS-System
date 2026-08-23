package com.tejas.service;

import com.tejas.domain.OrderStatus;
import com.tejas.domain.PaymentType;
import com.tejas.payload.dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrder(Long id);
    List<OrderDto> getOrdersByBranch(Long branchId,
                                    Long customerId,
                                    Long cashierId,
                                    PaymentType paymentType,
                                    OrderStatus status);
    void deleteOrder(Long id);
    List<OrderDto> getOrdersByCashier(Long cashierId);
    List<OrderDto> getTodayOrdersByBranch(Long branchId);
    List<OrderDto> getOrdersByCustomerId(Long customerId);
    List<OrderDto> getTop5RecentOrdersByBranchId(Long branchId);
}
