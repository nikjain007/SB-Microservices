package com.sbpractice.PaymentService.controller;

import com.sbpractice.PaymentService.model.PaymentRequest;
import com.sbpractice.PaymentService.model.PaymentResponse;
import com.sbpractice.PaymentService.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest){
        return new ResponseEntity<>(
                paymentService.doPayment(paymentRequest),
                HttpStatus.OK
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentDetailsByOrderId(@PathVariable String orderId){

        return new ResponseEntity<>(
                paymentService.getPaymentDetailsByOrderId(orderId),
                HttpStatus.OK
        );
    }
}
