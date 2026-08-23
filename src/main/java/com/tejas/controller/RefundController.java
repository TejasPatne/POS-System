package com.tejas.controller;

import com.tejas.payload.dto.RefundDto;
import com.tejas.payload.response.ApiResponse;
import com.tejas.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refunds")
public class RefundController {
    private final RefundService refundService;

    @PostMapping
    public ResponseEntity<RefundDto> create(@RequestBody RefundDto refundDto) {
        RefundDto refund = refundService.createRefund(refundDto);
        return new ResponseEntity<>(refund, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RefundDto>> getAllRefunds() {
        return new ResponseEntity<>(refundService.getAllRefunds(), HttpStatus.OK);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<RefundDto>> getRefundByBranchId(@PathVariable Long branchId) {
        List<RefundDto> refunds = refundService.getRefundsByBranch(branchId);
        return new ResponseEntity<>(refunds, HttpStatus.OK);
    }

    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<RefundDto>> getRefundByCashierId(@PathVariable Long cashierId) {
        List<RefundDto> refunds = refundService.getRefundsByCashier(cashierId);
        return new ResponseEntity<>(refunds, HttpStatus.OK);
    }

    @GetMapping("/shift/{shiftId}")
    public ResponseEntity<List<RefundDto>> getRefundByShiftId(@PathVariable Long shiftId) {
        List<RefundDto> refunds = refundService.getRefundsByShiftReport(shiftId);
        return new ResponseEntity<>(refunds, HttpStatus.OK);
    }

    @GetMapping("/cashier/{cashierId}/range")
    public ResponseEntity<List<RefundDto>> getRefundByCashierIdAndDateRange(@PathVariable Long cashierId,
                                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
                                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        List<RefundDto> refunds = refundService.getRefundsByCashierAndDateRange(cashierId, startDateTime, endDateTime);
        return new ResponseEntity<>(refunds, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RefundDto> getById(@PathVariable Long refundId) {
        RefundDto refund = refundService.getRefundById(refundId);
        return new ResponseEntity<>(refund, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long refundId) {
        refundService.deleteRefund(refundId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Refund deleted");
        return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
    }
}
