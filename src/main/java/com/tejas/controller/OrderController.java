package com.tejas.controller;

import com.tejas.domain.OrderStatus;
import com.tejas.domain.PaymentType;
import com.tejas.payload.dto.OrderDto;
import com.tejas.payload.response.ApiResponse;
import com.tejas.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody OrderDto orderDto) {
        OrderDto result = orderService.createOrder(orderDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> get(@PathVariable Long id) {
        OrderDto result = orderService.getOrder(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<OrderDto>> getOrdersByBranch(@PathVariable Long branchId,
                                                            @RequestParam(required = false) Long customerId,
                                                            @RequestParam(required = false) Long cashierId,
                                                            @RequestParam(required = false)PaymentType paymentType,
                                                            @RequestParam(required = false)OrderStatus status) {
        List<OrderDto> result = orderService.getOrdersByBranch(branchId, customerId, cashierId, paymentType, status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<OrderDto>> getOrdersByCashier(@PathVariable Long cashierId) {
        List<OrderDto> result = orderService.getOrdersByCashier(cashierId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/today/{branchId}")
    public ResponseEntity<List<OrderDto>> getTodayOrdersByBranch(@PathVariable Long branchId) {
        List<OrderDto> result = orderService.getTodayOrdersByBranch(branchId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<OrderDto> result = orderService.getOrdersByCustomerId(customerId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/branch/{branchId}/recent")
    public ResponseEntity<List<OrderDto>> getRecentOrdersOfBranch(@PathVariable Long branchId) {
        List<OrderDto> result = orderService.getTop5RecentOrdersByBranchId(branchId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("order deleted");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
