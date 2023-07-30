package com.sbpractice.ProductService.service;

import com.sbpractice.ProductService.entity.Product;
import com.sbpractice.ProductService.exception.ProductServiceCustomException;
import com.sbpractice.ProductService.model.ProductRequest;
import com.sbpractice.ProductService.model.ProductResponse;
import com.sbpractice.ProductService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.beans.BeanUtils.*;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding product..");
        Product product = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product created");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Getting product {}", productId);
        Product product
                = productRepository.findById(productId)
                .orElseThrow(()-> new ProductServiceCustomException("Product with given id is not present","PRODUCT_NOT_FOUND"));
        ProductResponse productResponse
                = new ProductResponse();
        copyProperties(product,productResponse);
        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce the quantity {} for product id {}", quantity, productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException(
                        "Product with given ID not found",
                        "PRODUCT_NOT_FOUND"
                ));
        if (product.getQuantity() < quantity) {
            throw new ProductServiceCustomException(
                    "Product do not have sufficient quantity",
                    "INSUFFICIENT_QUANTITY"
            );
         }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Product quantity updated successfully");
    }
}
