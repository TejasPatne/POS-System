package com.tejas.service.impl;

import com.tejas.mapper.RefundMapper;
import com.tejas.model.Branch;
import com.tejas.model.Order;
import com.tejas.model.Refund;
import com.tejas.model.User;
import com.tejas.payload.dto.RefundDto;
import com.tejas.repository.OrderRepository;
import com.tejas.repository.RefundRepository;
import com.tejas.service.RefundService;
import com.tejas.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {
    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    @Override
    public RefundDto createRefund(RefundDto refundDto) {
        User cashier = userService.getCurrentUser();

        Order order = orderRepository.findById(refundDto.getOrderId()).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        Branch branch = order.getBranch();

        Refund createdRefund = Refund.builder()
                .order(order)
                .cashier(cashier)
                .branch(branch)
                .reason(refundDto.getReason())
                .amount(refundDto.getAmount())
                .createdAt(refundDto.getCreatedAt())
                .build();
        Refund savedRefund = refundRepository.save(createdRefund);
        return RefundMapper.toDto(savedRefund);
    }

    @Override
    public List<RefundDto> getAllRefunds() {
        return refundRepository.findAll().stream().map(RefundMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<RefundDto> getRefundsByCashier(Long cashierId) {
        List<Refund> refunds = refundRepository.findByCashierId(cashierId);
        return refunds.stream().map(RefundMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<RefundDto> getRefundsByShiftReport(Long shiftReportId) {
        List<Refund> refunds = refundRepository.findByShiftReportId(shiftReportId);
        return refunds.stream().map(RefundMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<RefundDto> getRefundsByCashierAndDateRange(Long cashierId, LocalDateTime start, LocalDateTime end) {
        List<Refund> refunds = refundRepository.findByCashierIdAndCreatedAtBetween(cashierId, start, end);
        return refunds.stream().map(RefundMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<RefundDto> getRefundsByBranch(Long branchId) {
        List<Refund> refunds = refundRepository.findByBranchId(branchId);
        return refunds.stream().map(RefundMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public RefundDto getRefundById(Long refundId) {
        Refund refund = refundRepository.findById(refundId).orElseThrow(() -> new EntityNotFoundException("refund not found"));
        return RefundMapper.toDto(refund);
    }

    @Override
    public void deleteRefund(Long refundId) {
        Refund refund = refundRepository.findById(refundId).orElseThrow(() -> new EntityNotFoundException("refund not found"));
        refundRepository.delete(refund);
    }
}
