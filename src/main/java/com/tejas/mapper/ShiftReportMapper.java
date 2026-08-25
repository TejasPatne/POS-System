package com.tejas.mapper;

import com.tejas.model.Order;
import com.tejas.model.Product;
import com.tejas.model.Refund;
import com.tejas.model.ShiftReport;
import com.tejas.payload.dto.OrderDto;
import com.tejas.payload.dto.ProductDto;
import com.tejas.payload.dto.RefundDto;
import com.tejas.payload.dto.ShiftReportDto;

import java.util.List;
import java.util.stream.Collectors;

public class ShiftReportMapper {
    public static ShiftReportDto toDto(ShiftReport shiftReport) {
        return ShiftReportDto.builder()
                .id(shiftReport.getId())
                .shiftStart(shiftReport.getShiftStart())
                .shiftEnd(shiftReport.getShiftEnd())
                .totalSales(shiftReport.getTotalSales())
                .totalRefunds(shiftReport.getTotalRefunds())
                .netSale(shiftReport.getNetSale())
                .totalOrders(shiftReport.getTotalOrders())
                .cashier(UserMapper.toDto(shiftReport.getCashier()))
                .cashierId(shiftReport.getCashier().getId())
                .branchId(shiftReport.getBranch().getId())
                .recentOrders(mapOrders(shiftReport.getRecentOrders()))
                .topSellingProducts(mapProducts(shiftReport.getTopSellingProducts()))
                .refunds(mapRefunds(shiftReport.getRefunds()))
                .paymentSummaries(shiftReport.getPaymentSummaries())
                .build();
    }

    private static List<RefundDto> mapRefunds(List<Refund> refunds) {
        if (refunds == null || refunds.isEmpty()) return null;
        return refunds.stream().map(RefundMapper::toDto).collect(Collectors.toList());
    }

    private static List<ProductDto> mapProducts(List<Product> topSellingProducts) {
        if (topSellingProducts == null || topSellingProducts.isEmpty()) return null;
        return topSellingProducts.stream().map(ProductMapper::toDto).collect(Collectors.toList());
    }

    private static List<OrderDto> mapOrders(List<Order> recentOrders) {
        if (recentOrders == null || recentOrders.isEmpty()) return null;
        return recentOrders.stream().map(OrderMapper::toDto).collect(Collectors.toList());
    }
}
