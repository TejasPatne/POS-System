package com.tejas.service;

import com.tejas.payload.dto.RefundDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RefundService {
    RefundDto createRefund(RefundDto refundDto);
    List<RefundDto> getAllRefunds();
    List<RefundDto> getRefundsByCashier(Long cashierId);
    List<RefundDto> getRefundsByShiftReport(Long shiftReportId);
    List<RefundDto> getRefundsByCashierAndDateRange(Long cashierId, LocalDateTime start, LocalDateTime end);
    List<RefundDto> getRefundsByBranch(Long branchId);
    RefundDto getRefundById(Long refundId);
    void deleteRefund(Long refundId);
}
