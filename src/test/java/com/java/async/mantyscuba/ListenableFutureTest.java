package com.java.async.mantyscuba;

import org.junit.jupiter.api.Test;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by KMS on 16/04/2020.
 */
public class ListenableFutureTest {

    Callable task = () -> {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("TASK completed");
        return null;
    };

    @Test
    public void listenableFuture() throws Exception {
        ListenableFutureTask listenableFutureTask = new ListenableFutureTask(task);
        listenableFutureTask.addCallback(new ListenableFutureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("exception occurred!!");
            }

            @Override
            public void onSuccess(Object o) {
                ListenableFutureTask listenableFuture = new ListenableFutureTask(task);
                listenableFuture.addCallback(new ListenableFutureCallback() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println("exception occurred!!");
                    }

                    @Override
                    public void onSuccess(Object o) {
                        System.out.println("all tasks completed!!");
                    }
                });
                listenableFuture.run();
            }
        });
        listenableFutureTask.run();

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
