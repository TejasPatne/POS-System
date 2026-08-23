package com.tejas.repository;

import com.tejas.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RefundRepository extends JpaRepository<Refund, Long> {
    List<Refund> findByCashierIdAndCreatedAtBetween(Long cashierId, LocalDateTime from, LocalDateTime to);
    List<Refund> findByCashierId(Long cashierId);
    List<Refund> findByShiftReportId(Long cashierId);
    List<Refund> findByBranchId(Long cashierId);

}
