package com.java.async.mantyscuba;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by KMS on 20/04/2020.
 */
@Slf4j
public class ThenApplyTest {

    @Test
    public void completableFutureThenApplyTest() {
        // Create a CompletableFuture
        CompletableFuture<String> whatsYourNameFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Rajeev";
        });

        // Attach a callback to the Future using thenApply()
        CompletableFuture<String> greetingFuture = whatsYourNameFuture.thenApply(name -> {
            return "Hello " + name;
        });

        // Block and get the result of the future.
        try {
            log.info(greetingFuture.get()); // Hello Rajeev
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        CompletableFuture<String> welcomeText = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            }
            catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Rajeev";
        }).thenApply(name -> {
            return "Hello " + name;
        }).thenApply(greeting -> {
            return greeting + ", Welcome to the CalliCoder Blog";
        });

        try {
            log.info(welcomeText.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
