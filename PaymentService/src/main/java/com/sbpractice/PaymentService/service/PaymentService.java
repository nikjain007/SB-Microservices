package com.sbpractice.PaymentService.service;

import com.sbpractice.PaymentService.model.PaymentRequest;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);
}
