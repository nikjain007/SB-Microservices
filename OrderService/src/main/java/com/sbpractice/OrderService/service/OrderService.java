package com.sbpractice.OrderService.service;

import com.sbpractice.OrderService.model.OrderRequest;
import com.sbpractice.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
