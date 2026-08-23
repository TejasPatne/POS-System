package com.tejas.payload.dto;

import com.tejas.domain.PaymentType;
import com.tejas.model.ShiftReport;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RefundDto {
    private Long id;
    private OrderDto order;
    private Long orderId;
    private String reason;
    private Double amount;
    private ShiftReport shiftReport;
    private Long shiftReportId;
    private UserDto cashier;
    private String cashierName;
    private BranchDto branch;
    private Long branchId;
    private PaymentType paymentType;
    private LocalDateTime createdAt;

}
