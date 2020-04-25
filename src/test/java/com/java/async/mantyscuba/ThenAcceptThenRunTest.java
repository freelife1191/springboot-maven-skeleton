package com.java.async.mantyscuba;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

/**
 * Created by KMS on 20/04/2020.
 */
@Slf4j
public class ThenAcceptThenRunTest {

    // thenAccept() 를 위한 model 예제 클래스
    @Data
    public static class Product {
        int id;
        String name;

        public Product(int id, String name) {
            this.id = id; this.name = name;
        }
    }

    // thenAccept() 를 위한 service 예제 클래스
    public static class ProductService {
        private ProductService() {};
        static Product getProductDetail(int productId) {
            Product product = new Product(productId, "name_"+productId);
            System.out.println("productId = " + productId); return product;
        }
    }

    @Test
    public void completableFutureThenAcceptTest() {
        int productId = 123;
        // thenAccept() example
        CompletableFuture.supplyAsync(() -> {
            log.info("before call getProductDetail()");
            return ProductService.getProductDetail(productId);
        }).thenAccept(product -> {
            log.info("Got product detail from remote service " + product.getName());
        });

        int productId2 = 999;
        // thenRun() example
        CompletableFuture.supplyAsync(() -> {
            return ProductService.getProductDetail(productId2);
        }).thenRun(() -> {
            log.info("thenRun() executed");
        });
    }

}
