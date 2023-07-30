package com.sbpractice.OrderService.service;

import com.netflix.discovery.converters.Auto;
import com.sbpractice.OrderService.entity.Order;
import com.sbpractice.OrderService.external.client.ProductService;
import com.sbpractice.OrderService.model.OrderRequest;
import com.sbpractice.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sbpractice.CommonService.client.ProduceService;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;
    @Override
    public long placeOrder(OrderRequest orderRequest) {

        log.info("Placing order request: {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating the order with Status created");

        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();


        order = orderRepository.save(order);
        log.info("Order placed successfully with order Id : {}", order.getProductId());

        return order.getId();
    }
}
