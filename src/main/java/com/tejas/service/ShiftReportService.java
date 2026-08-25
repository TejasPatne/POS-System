package com.tejas.service;

import com.tejas.payload.dto.ShiftReportDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ShiftReportService {
    ShiftReportDto startShift();
    ShiftReportDto endShift(Long shiftReportId, LocalDateTime shiftEnd);
    ShiftReportDto getShiftReportById(Long id);
    List<ShiftReportDto> getAllShiftReports();
    List<ShiftReportDto> getShiftReportsByBranchId(Long branchId);
    List<ShiftReportDto> getShiftReportsByCashierId(Long cashierId);
    ShiftReportDto getCurrentShiftProgress(Long cashierId);
    ShiftReportDto getShiftByCashierAndDate(Long cashierId, LocalDateTime date);

}
