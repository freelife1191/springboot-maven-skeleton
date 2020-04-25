package com.java.async.mantyscuba;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Created by KMS on 20/04/2020.
 */
@Slf4j
public class CompleteRunAsyncSupplyAsyncTest {

    @Test
    public void completableFutureTest() {
        CompletableFuture<String> future = new CompletableFuture<>();
        try {
            TimeUnit.SECONDS.sleep(2);
            future.complete("Complete Stated!"); // 완료상태 표시
            System.out.println("Before get()");
            System.out.println("future_get = " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void completableFutureRunAsyncTest() {
        log.info("start.");
        // Using Lambda Expression
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // Simulate a long-running Job
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            log.info("I'll run in a separate thread than the main thread.");
        });

        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void completableFutureSupplyAsyncTest() {
        // Run a task specified by a Supplier object asynchronously
        CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
                return "Result of the asynchronous computation";
            }
        });

        // Block and get the result of the Future
        String result = null;
        try {
            result = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } log.info("get_result : " + result);
    }


}
