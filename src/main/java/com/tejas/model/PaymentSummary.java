package com.tejas.model;

import com.tejas.domain.PaymentType;
import lombok.Data;

@Data
public class PaymentSummary {
    private PaymentType paymentType;
    private Double totalAmount;
    private int transactionCount;
    private Double percentage;
}
