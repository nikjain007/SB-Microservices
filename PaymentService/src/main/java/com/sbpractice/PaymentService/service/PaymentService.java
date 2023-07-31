package com.sbpractice.PaymentService.service;

import com.sbpractice.PaymentService.model.PaymentRequest;
import com.sbpractice.PaymentService.model.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(String orderId);
}
