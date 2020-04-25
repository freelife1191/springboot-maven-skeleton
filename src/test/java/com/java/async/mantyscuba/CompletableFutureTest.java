package com.java.async.mantyscuba;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by KMS on 16/04/2020.
 */
public class CompletableFutureTest {

    /**
     * thenCompose
     * CompletableFuture 를 반환하는 Method를 Chain으로 실행하고 싶을때
     * 즉 이전에 Async 프로세스로 응답 받은 값을 다음 Async 프로세스의 인자로 사용하는 경우에 아래와 같이 thenCompose, thenComposeAync 를 사용할 수 있다
     * 비동기처리 Return 값을 다음 처리의 Parameter 로 사용할때 사용한다
     *
     * 결과 각 프로세스는 순차적으로 진행됨을 확인
     * input, output 사이에 "Non Blocking!!" 표시되는 것으로 보아 thread에 Blocking 없음
     * @throws Exception
     */
    @Test
    public void thenComposeTest() throws Exception {
        Price price = new Price();
        price.getPriceAsync(1)
                .thenComposeAsync(price::getPriceAsync)
                .thenComposeAsync(price::getPriceAsync)
                .thenComposeAsync(r -> price.getPriceAsync(r));

        System.out.println("Non Bolocking!!");

        // main thread 가 죽으면 child 도 다 죽어 버려서 대기함
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    /**
     * thenCombine
     * 두가지 프로세스를 parallel 하게 동시에 진행하고 결과 값을 조합한 처리를 할때
     * 두개의 비동기 요청을 동시에 진행해서 조합 할 수 있다
     *
     * 결과 각 프로세스는 순차적으로 진행됨을 확인
     * input, output 사이에 "Non Blocking!!" 표시되는 것으로 보아 thread에 Blocking 은 없음
     * @throws Exception
     */
    @Test
    public void thenCombineTest() throws Exception {
        Price price = new Price();
        CompletableFuture<Double> price1 = price.getPriceAsync(1);
        CompletableFuture<Double> price2 = price.getPriceAsync(2);
        price2.thenCombineAsync(price1, (a, b) -> a + b)
                .thenAcceptAsync(System.out::print);

        System.out.println("Non Blocking!!");

        // main thread 가 죽으면 child 도 다 죽어 버려서 대기함
        Thread.sleep(5000);
    }

    /**
     * thenApply(Async)
     * ListenableFuture 에서는 Callback 을 설정하는 것과 동일한 작업
     * thenApply or thenApplyAsync 는 Function 을 인자로 받기 때문에 다음 후속작업에 결과값을 return 해 줄 수 있다
     */

    /**
     * thenAccept(Async)
     * ListenableFuture 에서는 Callback 을 설정하는 것과 동일한 작업이지만 Consumer 인터페이스가 인자이기 떄문에 후속 작업에 결과값을 return 할 수 없다
     */

    /**
     * exceptionally
     * 지금까지 실행된 completablefuture에서 발행한 Throwable 을 처리할 수 있다
     * ListenableFuture 등에서 Callback.onFailure 의 역할을 대신할 수 있다
     * 기존 ListenableFuture 는 각 요청별로 실패에 대한 Callback 을 설정한 반면 CompletableFuture는 모든 Exception을 통합적으로 처리할 수 있다
     */

    static class Price {
        public double getPrice(double oldprice) throws Exception {
            return calculatePrice(oldprice);
        }

        public double calculatePrice(double oldprice) throws Exception {
            System.out.println("Input : " + oldprice);
            Thread.sleep(1000);
            System.out.println("Output : " + (oldprice + 1));
            return oldprice + 1;
        }

        public CompletableFuture<Double> getPriceAsync(double oldPrice) {
            CompletableFuture<Double> completableFuture = new CompletableFuture<>();
            new Thread(() -> {
                try {
                    double price = calculatePrice(oldPrice);
                    completableFuture.complete(price);
                } catch (Exception ex) {
                    completableFuture.completeExceptionally(ex);
                }
            }).start();

            return completableFuture;
        }
    }

}