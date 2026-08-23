package com.tejas.mapper;

import com.tejas.model.Refund;
import com.tejas.payload.dto.RefundDto;

public class RefundMapper {
    public static RefundDto toDto(Refund refund) {
        return RefundDto.builder()
                .id(refund.getId())
                .orderId(refund.getOrder() != null ? refund.getOrder().getId() : null)
                .reason(refund.getReason())
                .amount(refund.getAmount())
                .shiftReportId(refund.getShiftReport() != null ? refund.getShiftReport().getId() : null)
                .cashierName(refund.getCashier() != null ? refund.getCashier().getFullName() : null)
                .branchId(refund.getBranch() != null ? refund.getBranch().getId() : null)
                .paymentType(refund.getPaymentType())
                .build();
    }
}
