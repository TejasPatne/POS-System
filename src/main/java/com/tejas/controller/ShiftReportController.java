package com.tejas.controller;

import com.tejas.payload.dto.ShiftReportDto;
import com.tejas.service.ShiftReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shift-reports")
public class ShiftReportController {
    private final ShiftReportService shiftReportService;

    @PostMapping("/start")
    public ResponseEntity<ShiftReportDto> startShift() {
        return new ResponseEntity<>(shiftReportService.startShift(), HttpStatus.OK);
    }

    @PostMapping("/end")
    public ResponseEntity<ShiftReportDto> endShift() {
        return new ResponseEntity<>(shiftReportService.endShift(null, null), HttpStatus.OK);
    }

    @PostMapping("/current")
    public ResponseEntity<ShiftReportDto> getCurrentShiftProgress() {
        return new ResponseEntity<>(shiftReportService.getCurrentShiftProgress(null), HttpStatus.OK);
    }

    @PostMapping("/cashier/{cashierId}/by-date")
    public ResponseEntity<ShiftReportDto> getShiftReportByDate(@PathVariable Long cashierId,
                                                               @RequestParam @DateTimeFormat (iso = DateTimeFormat.ISO.DATE) LocalDateTime date) {
        return new ResponseEntity<>(shiftReportService.getShiftByCashierAndDate(cashierId, date), HttpStatus.OK);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<ShiftReportDto>> getShiftReportByBranch(@PathVariable Long branchId) {
        return new ResponseEntity<>(shiftReportService.getShiftReportsByBranchId(branchId), HttpStatus.OK);
    }


    @GetMapping("/cashier/{cashierId}")
    public ResponseEntity<List<ShiftReportDto>> getShiftReportByCashier(@PathVariable Long cashierId) {
        return new ResponseEntity<>(shiftReportService.getShiftReportsByCashierId(cashierId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftReportDto> getShiftReportById(@PathVariable Long id) {
        return new ResponseEntity<>(shiftReportService.getShiftReportById(id), HttpStatus.OK);
    }
}
