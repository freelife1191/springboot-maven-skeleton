package com.java.async.mantyscuba;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by KMS on 20/04/2020.
 */
@Slf4j
public class ThenComposeTest {

    @Data
    public static class User {
        String userId;
        String name;
        double score;
    }

    public static class UserService {
        private UserService() {};
        static User getUserDetails(String userId) {
            User user = new User();
            System.out.println("user = " + userId);
            return user;
        }
    }

    public static class CreditRatingService {
        static Double getCreditRating(User user) {
            double val = 1.2f;
            return user.getScore() + val;
        }
    }

    CompletableFuture<User> getUserDetail(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            return UserService.getUserDetails(userId);
        });
    }

    CompletableFuture<Double> getCreditRating(User user) {
        return CompletableFuture.supplyAsync(() -> {
            return CreditRatingService.getCreditRating(user);
        });
    }

    @Test
    public void completableFutureThenCompseTest() {
        String userId = "testUser";
        CompletableFuture<CompletableFuture<Double>> result = getUserDetail(userId)
                .thenApply(user -> getCreditRating(user));
        log.info("here");

        try {
            log.info("result = " + result.get().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        CompletableFuture<Double> result2 = getUserDetail(userId)
                .thenCompose(user -> getCreditRating(user));
        try {
            System.out.println("result2 = " + result2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
