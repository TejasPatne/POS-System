package com.tejas.service.impl;

import com.tejas.domain.PaymentType;
import com.tejas.mapper.ShiftReportMapper;
import com.tejas.model.*;
import com.tejas.payload.dto.ShiftReportDto;
import com.tejas.repository.OrderRepository;
import com.tejas.repository.RefundRepository;
import com.tejas.repository.ShiftReportRepository;
import com.tejas.service.ShiftReportService;
import com.tejas.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftReportServiceImpl implements ShiftReportService {
    private final ShiftReportRepository shiftReportRepository;
    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    @Override
    public ShiftReportDto startShift() {
        User currentUser = userService.getCurrentUser();
        LocalDateTime shiftStart = LocalDateTime.now();
        LocalDateTime startOfTheDay = shiftStart.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfTheDay = shiftStart.withHour(23).withMinute(59).withSecond(59);

        Optional<ShiftReport> existing = shiftReportRepository.findByCashierAndShiftStartBetween(currentUser, startOfTheDay, endOfTheDay);

        if (existing.isPresent()) {
            throw new RuntimeException("Shift already started, go on work bro!");
        }

        Branch branch = currentUser.getBranch();

        ShiftReport savedShiftReport = ShiftReport.builder().cashier(currentUser).shiftStart(shiftStart).branch(branch).build();

        return ShiftReportMapper.toDto(savedShiftReport);
    }

    @Override
    public ShiftReportDto endShift(Long shiftReportId, LocalDateTime shiftEnd) {
        User currentUser = userService.getCurrentUser();
        ShiftReport shiftReport = shiftReportRepository.findTopByCashierAndShiftEndIsNullOrderByShiftStartDesc(currentUser)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        shiftReport.setShiftEnd(shiftEnd);

        List<Refund> refunds = refundRepository.findByCashierIdAndCreatedAtBetween(currentUser.getId(), shiftReport.getShiftStart(), shiftReport.getShiftEnd());
        double totalRefunds = refunds.stream().mapToDouble(refund -> refund.getAmount() != null ? refund.getAmount() : 0.0).sum();
        List<Order> orders = orderRepository.findByCashierIdAndCreatedAtBetween(currentUser.getId(), shiftReport.getShiftStart(), shiftReport.getShiftEnd());
        double totalSales = orders.stream().mapToDouble(Order::getTotalAmount).sum();
        int totalOrders = orders.size();
        double netSale = totalSales - totalRefunds;

        shiftReport.setTotalRefunds(totalRefunds);
        shiftReport.setTotalOrders(totalOrders);
        shiftReport.setTotalSales(totalSales);
        shiftReport.setNetSale(netSale);
        shiftReport.setTopSellingProducts(getTopSellingProducts(orders));
        shiftReport.setRecentOrders(getRecentOrders(orders));
        shiftReport.setPaymentSummaries(getPaymentSummaries(orders, totalSales));
        shiftReport.setRefunds(refunds);

        ShiftReport savedReport = shiftReportRepository.save(shiftReport);
        return ShiftReportMapper.toDto(savedReport);
    }

    @Override
    public ShiftReportDto getShiftReportById(Long id) {
        ShiftReport shiftReport =  shiftReportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("shift report not found"));
        return ShiftReportMapper.toDto(shiftReport);
    }

    @Override
    public List<ShiftReportDto> getAllShiftReports() {
        List<ShiftReport> shiftReports = shiftReportRepository.findAll();
        return shiftReports.stream()
                .map(ShiftReportMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftReportDto> getShiftReportsByBranchId(Long branchId) {
        List<ShiftReport> shiftReports = shiftReportRepository.findByBranchId(branchId);
        return shiftReports.stream()
                .map(ShiftReportMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShiftReportDto> getShiftReportsByCashierId(Long cashierId) {
        List<ShiftReport> shiftReports = shiftReportRepository.findByCashierId(cashierId);
        return shiftReports.stream()
                .map(ShiftReportMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ShiftReportDto getCurrentShiftProgress(Long cashierId) {
        User user = userService.getCurrentUser();
        ShiftReport shiftReport = shiftReportRepository.findTopByCashierAndShiftEndIsNullOrderByShiftStartDesc(user)
                .orElseThrow(() -> new RuntimeException("no active shift found for cashier"));
        List<Order> orders = orderRepository.findByCashierIdAndCreatedAtBetween(user.getId(), shiftReport.getShiftStart(), LocalDateTime.now());

        List<Refund> refunds = refundRepository.findByCashierIdAndCreatedAtBetween(user.getId(), shiftReport.getShiftStart(), shiftReport.getShiftEnd());
        double totalRefunds = refunds.stream().mapToDouble(refund -> refund.getAmount() != null ? refund.getAmount() : 0.0).sum();
        double totalSales = orders.stream().mapToDouble(Order::getTotalAmount).sum();
        int totalOrders = orders.size();
        double netSale = totalSales - totalRefunds;

        shiftReport.setTotalRefunds(totalRefunds);
        shiftReport.setTotalOrders(totalOrders);
        shiftReport.setTotalSales(totalSales);
        shiftReport.setNetSale(netSale);
        shiftReport.setTopSellingProducts(getTopSellingProducts(orders));
        shiftReport.setRecentOrders(getRecentOrders(orders));
        shiftReport.setPaymentSummaries(getPaymentSummaries(orders, totalSales));
        shiftReport.setRefunds(refunds);

        ShiftReport savedReport = shiftReportRepository.save(shiftReport);
        return ShiftReportMapper.toDto(savedReport);
    }

    @Override
    public ShiftReportDto getShiftByCashierAndDate(Long cashierId, LocalDateTime date) {
        User cashier = userService.getUserById(cashierId);

        LocalDateTime start = date.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = date.withHour(23).withMinute(59).withSecond(59);

        ShiftReport report = shiftReportRepository.findByCashierAndShiftStartBetween(cashier, start, end)
                .orElseThrow(() -> new RuntimeException("shift report not found"));
        return ShiftReportMapper.toDto(report);
    }

    private List<PaymentSummary> getPaymentSummaries(List<Order> orders, double totalSales) {
        Map<PaymentType, List<Order>> ordersOfPaymentType = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getPaymentType() != null ? order.getPaymentType() : PaymentType.CASH));

        List<PaymentSummary> paymentSummaries = new ArrayList<>();
        for (Map.Entry<PaymentType, List<Order>> entry: ordersOfPaymentType.entrySet()) {
            double amount = entry.getValue().stream()
                    .mapToDouble(Order::getTotalAmount).sum();

            int transactions = entry.getValue().size();
            double percentage = (amount/totalSales) * 100;

            PaymentSummary ps = new PaymentSummary();
            ps.setPaymentType(entry.getKey());
            ps.setTotalAmount(amount);
            ps.setTransactionCount(transactions);
            ps.setPercentage(percentage);
            paymentSummaries.add(ps);
        }
        return paymentSummaries;
    }

    private List<Product> getTopSellingProducts(List<Order> orders) {
        Map<Product, Integer> productSalesMap = new HashMap<>();

        for (Order order: orders) {
            for (OrderItem item: order.getItems()) {
                Product product = item.getProduct();
                productSalesMap.put(product,
                        productSalesMap.getOrDefault(product, 0) + item.getQuantity());
            }
        }

        return productSalesMap.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<Order> getRecentOrders(List<Order> orders) {
        return orders.stream()
                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }
}
