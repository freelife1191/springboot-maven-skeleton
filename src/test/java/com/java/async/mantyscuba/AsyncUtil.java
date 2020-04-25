package com.java.async.mantyscuba;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;

/**
 * ListenableFuture 를 CompletableFuture로 변환하기
 * 아래와 같이 ListenableFuture를 CompletableFuture 로 변환할 수 있다
 *
 * 하지만 스프링프레임워크 5.0 부터 completable() 이라는 메소드를 호출하면 간단히 CompletableFuture로 변환 할 수 있다
 * Created by KMS on 16/04/2020.
 */
public class AsyncUtil {

    public static <T> CompletableFuture<T> buildCompletableFuture(ListenableFuture<T> listenableFuture) {
        CompletableFuture<T> completableFuture = new CompletableFuture<T>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                boolean result = listenableFuture.cancel(mayInterruptIfRunning);
                super.cancel(mayInterruptIfRunning);
                return result;
            }
        };
        listenableFuture.addCallback(new ListenableFutureCallback<T>() {
            @Override
            public void onFailure(Throwable ex) {
                completableFuture.completeExceptionally(ex);
            }

            @Override
            public void onSuccess(T result) {
                completableFuture.complete(result);
            }
        });
        return completableFuture;
    }

    public static <T> CompletableFuture<T> doNothingCompletableFuture() {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        completableFuture.complete(null);
        return completableFuture;
    }
}
