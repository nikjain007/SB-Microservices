package com.sbpractice.OrderService.service;

import com.sbpractice.OrderService.model.OrderRequest;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
}
