package com.java.async.mantyscuba;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by KMS on 16/04/2020.
 */
public class completableFuture {

    Runnable task = () -> {
        try {
            TimeUnit.SECONDS.sleep(5);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("TASK completed");
    };

    @Test
    @DisplayName(" 5초 동안 쉬었다가 성공 로그를 찍는 TASK 를 순차적으로 실행시키고 마지막에 \"all tasks completed\" 를 표시")
    public void completableFuture() throws Exception {
        CompletableFuture
                .runAsync(task)
                .thenCompose(aVoid -> CompletableFuture.runAsync(task))
                .thenAcceptAsync(aVoid -> System.out.println("all tasks completed!!"))
                .exceptionally(throwable -> {
                    System.out.println("exception occurred!!");
                    return null;
                });

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
