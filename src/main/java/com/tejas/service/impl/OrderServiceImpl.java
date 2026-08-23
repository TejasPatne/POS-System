package com.tejas.service.impl;

import com.tejas.domain.OrderStatus;
import com.tejas.domain.PaymentType;
import com.tejas.mapper.OrderMapper;
import com.tejas.model.*;
import com.tejas.payload.dto.OrderDto;
import com.tejas.repository.OrderRepository;
import com.tejas.repository.ProductRepository;
import com.tejas.service.OrderService;
import com.tejas.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        User cashier = userService.getCurrentUser();
        Branch branch = cashier.getBranch();
        if (branch == null) {
            throw new RuntimeException("Cashier's branch not found");
        }
        Order order = Order.builder()
                .branch(branch)
                .cashier(cashier)
                .customer(orderDto.getCustomer())
                .paymentType(orderDto.getPaymentType())
                .build();

        List<OrderItem> orderItems = orderDto.getItems().stream().map(
                itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product not found"));
                    return OrderItem.builder()
                            .product(product)
                            .quantity(itemDto.getQuantity())
                            .price(product.getSellingPrice() * itemDto.getQuantity())
                            .order(order)
                            .build();
                }
        ).toList();
        double total = orderItems.stream().mapToDouble(OrderItem::getPrice).sum();
        order.setTotalAmount(total);
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto getOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("order not found"));
        return OrderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getOrdersByBranch(Long branchId, Long customerId, Long cashierId, PaymentType paymentType, OrderStatus status) {
        return orderRepository.findByBranchId(branchId).stream()
                .filter(order -> customerId == null || order.getCustomer() != null && (order.getCustomer().getId().equals(cashierId)))
                .filter(order -> cashierId == null || order.getCashier() != null && (order.getCashier().getId().equals(cashierId)))
                .filter(order -> paymentType == null || order.getPaymentType() == paymentType)
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersByCashier(Long cashierId) {
        return orderRepository.findByCashierId(cashierId)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getTodayOrdersByBranch(Long branchId) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();
        return orderRepository.findByBranchIdAndCreatedAtBetween(branchId, start, end)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getTop5RecentOrdersByBranchId(Long branchId) {
        return orderRepository.findTop5ByBranchIdOrderByCreatedAtDesc(branchId)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }
}
