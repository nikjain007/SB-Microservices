package com.sbpractice.ProductService.service;

import com.sbpractice.ProductService.model.ProductRequest;
import com.sbpractice.ProductService.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
