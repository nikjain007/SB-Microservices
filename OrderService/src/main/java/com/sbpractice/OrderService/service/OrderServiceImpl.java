package com.sbpractice.OrderService.service;

import brave.messaging.ProducerResponse;
import com.netflix.discovery.converters.Auto;
import com.sbpractice.OrderService.entity.Order;
import com.sbpractice.OrderService.exception.CustomException;
import com.sbpractice.OrderService.external.client.PaymentService;
import com.sbpractice.OrderService.external.client.ProductService;
import com.sbpractice.OrderService.external.request.PaymentRequest;
import com.sbpractice.OrderService.model.OrderRequest;
import com.sbpractice.OrderService.model.OrderResponse;
import com.sbpractice.OrderService.repository.OrderRepository;
import com.sbpractice.ProductService.model.ProductResponse;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sbpractice.CommonService.client.ProduceService;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

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

        log.info("Calling Payment service to complete the payment");

        PaymentRequest paymentRequest =
                PaymentRequest.builder()
                        .orderId(order.getId())
                                .paymentMode(orderRequest.getPaymentMode())
                                        .amount(orderRequest.getTotalAmount())
                                                .build();

        String orderStatus = null;
        try{

            paymentService.doPayment(paymentRequest);
            log.info("Payment is done successfully, changing the order status to PLACED");
            orderStatus = "PLACED";

        } catch (Exception e){
            log.error("Error occurred in payment, changing order status to FAILED");
            orderStatus = "FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        log.info("Order placed successfully with order Id : {}", order.getProductId());

        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {

        log.info("Get order details for orderId:{}", orderId);
            Order order = orderRepository
                    .findById(orderId)
                    .orElseThrow(() -> new CustomException("Order not found for the order Id:" + orderId,
                            "NOT_FOUND",
                            404));

            log.info("Invoking product service to catch product details of id:{}", order.getProductId());

            ProductResponse productResponse =
                    restTemplate.getForObject("http://PRODUCT-SERVICE/product/"+order.getProductId(),
                            ProductResponse.class);



            OrderResponse.ProductDetails productDetails =
                      OrderResponse.ProductDetails
                              .builder()
                              .productName(productResponse.getProductName())
                              .productId(productResponse.getProductId())
                              .build();

            OrderResponse orderResponse =
                    OrderResponse
                            .builder()
                            .orderId(order.getId())
                            .orderStatus(order.getOrderStatus())
                            .amount(order.getAmount())
                            .orderDate(order.getOrderDate())
                            .productDetails(productDetails)
                            .build();

            return orderResponse;

    }
}
