package com.java.async.logicbig;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalTime;
import java.util.concurrent.*;

/**
 * Java-CompletionStage 및 CompletableFuture의 기초
 * https://www.logicbig.com/tutorials/core-java-tutorial/java-multi-threading/completion-stage-and-completable-future.html
 *
 * CompletionStage Interface
 * java.util.concurrent.CompletionStage<T>인터페이스는 정류 작업 (동기 또는 비동기)을 나타냅니다
 * 이 인터페이스에 선언 된 모든 메소드가 CompletionStage자체 인스턴스를 리턴하므로 여러 CompletionStages태스크를 서로 다른 방식으로 연결하여 태스크 그룹을 완료 할 수 있습니다
 * Created by KMS on 17/04/2020.
 */
public class CompletionStageAndCompletableFuture {

    /**
     * CompletableFuture 클래스
     * CompletableFuture<T> implements CompletionStage<T> and Future<T>
     *이 클래스의 정적 팩토리 메소드는 태스크 실행의 시작점입니다
     * 기본 수준에서 시작되는 작업 CompletableFuture은 두 가지 범주로 나눌 수 있습니다
     * 결과를 반환하지 않는 작업
     * 결과를 반환하는 작업
     */

    /**
     * 결과를 반환하지 않고 작업 생성 및 실행
     * 다음과 같은 정적 팩토리 메소드를 CompletableFuture사용하여 제공된을 통해 태스크를 실행할 수 있습니다 Runnable
     * public static CompletableFuture<Void> runAsync(Runnable runnable)
     * public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor)
     *
     * RESULT
     * running, in thread: ForkJoinPool.commonPool-worker-3
     * main exiting, thread: main
     */
    @Test
    public void runExample() {
        CompletableFuture<Void> cf =
                CompletableFuture.runAsync(() -> {
                    System.out.println("running, in thread: " + Thread.currentThread().getName());
                });
        cf.join();//waits until task is completed
        System.out.println("main exiting, thread: "+Thread.currentThread().getName());
    }

    /**
     * CompletableFuture.join() 및 Future.get()
     * join() 메소드는 다음과 같이 completableFuture에 정의됩니다
     * public T join()
     *
     * 완료되면 대기하고 결과 값을 리턴합니다
     * get() 메소드는 Future 인터페이스에서 정의되며 CompletableFuture에서 재정의됩니다
     * public T get() throws InterruptedException, ExecutionException
     *
     * 이 메소드는 또한이 미래가 완료 될 때까지 기다린 다음 결과를 리턴합니다
     * join() 및 get() 메서드는 서로 바꿔 사용할 수 있습니다
     * 가장 큰 차이점은 join() 메서드가 확인 된 예외를 throw하지 않아 패턴이 더 단순하다는 것입니다
     *
     * CompletionStage(s)에 runAsync()로 연결
     * CompletionStage runnable을 수용하는 다음과 같은 방법 으로 체인화
     * CompletionStage<Void> thenRun(Runnable action)
     * CompletionStage<Void> thenRunAsync(Runnable action)
     * CompletionStage<Void> thenRunAsync(Runnable action, Executor executor)
     *
     * RESULT
     * ---------
     * stage: first stage, time before task: 17:39:13.018213700, thread: ForkJoinPool.commonPool-worker-3
     * stage: first stage, time after task: 17:39:14.026771300, thread: ForkJoinPool.commonPool-worker-3
     * ---------
     * stage: second stage, time before task: 17:39:14.026771300, thread: ForkJoinPool.commonPool-worker-3
     * stage: second stage, time after task: 17:39:15.027458600, thread: ForkJoinPool.commonPool-worker-3
     * ---------
     * stage: third stage, time before task: 17:39:15.030441900, thread: ForkJoinPool.commonPool-worker-3
     * stage: third stage, time after task: 17:39:16.031504, thread: ForkJoinPool.commonPool-worker-3
     * main exiting
     */
    @Test
    public void runExample2() {
        CompletionStage<Void> cf =
                CompletableFuture.runAsync(() -> performTask("first stage"));
        cf = cf.thenRun(() -> performTask("second stage"));
        cf = cf.thenRunAsync(() -> performTask("third stage"));
        ((CompletableFuture) cf).join();//waits until task is completed
        System.out.println("main exiting");
    }

    /**
     * CompletableFuture은 기본적으로 사용 Executor에 의해 생성 ForkJoinPool.commonPool()(병렬 처리가 지원되지 않는 경우에, 새로운 Thread가 각각의 작업을 실행하기 위해 생성된다)
     * 위의 예제에서 두 개의 비동기 작업은 동일한 스레드에서 실행되었습니다 (예 : 기본 실행기에 의해 생성됨). ForkJoinPool.commonPool-worker-3
     *
     * 비동기 단계의 경우 작업이 이전 단계를 완료하는 스레드에서 실행됩니다
     * 기본값에 의존하지 않고 Executor오버로드 된 메소드를 사용하여 자체 실행기를 제공 할 수도 있습니다 runAsync(Runnable runnable, Executor executor)
     *
     * 아래의 예에서 중간 변수도 생략하고 적절한 메소드 체인 구문을 사용했습니다
     *
     * RESULT
     * ---------
     * stage: first stage, time before task: 15:48:58.797553100, thread: pool-1-thread-1
     * stage: first stage, time after task: 15:48:59.809014600, thread: pool-1-thread-1
     * ---------
     * stage: second stage, time before task: 15:48:59.810009, thread: pool-1-thread-1
     * stage: second stage, time after task: 15:49:00.811988700, thread: pool-1-thread-1
     * ---------
     * stage: third stage, time before task: 15:49:00.813970800, thread: pool-1-thread-2
     * stage: third stage, time after task: 15:49:01.814515500, thread: pool-1-thread-2
     * main exiting
     */
    @Test
    public void runExample3() {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        CompletableFuture.runAsync(() -> performTask("first stage"), executor)
                .thenRun(() -> performTask("second stage"))
                .thenRunAsync(() -> performTask("third stage"), executor)
                .join();//waits until task is completed
        System.out.println("main exiting");
        executor.shutdown();
    }

    private void performTask(String stage) {
        System.out.println("---------");
        System.out.printf("stage: %s, time before task: %s, thread: %s%n",
                stage, LocalTime.now(), Thread.currentThread().getName());
        try {
            //simulating long task
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("stage: %s, time after task: %s, thread: %s%n",
                stage, LocalTime.now(), Thread.currentThread().getName());
    }

    /**
     * 결과를 반환하는 작업 만들기 및 실행
     * 다음 방법을 CompletableFuture 사용하여 결과를 반환하는 작업을 시작할 수 있습니다
     * public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
     * public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor)
     *
     * RESULT
     * 7
     */
    @Test
    public void supplyExample() {
        CompletableFuture<Integer> cf =
                CompletableFuture.supplyAsync(() -> ThreadLocalRandom.current().nextInt(1, 10));
        Integer integer = cf.join();//similar to cf.get()
        System.out.println(integer);
    }

    /**
     * supplyAsync()를 사용하여 연결 완료 단계
     * 이후 CompletableFuture.supplyAsync는 () 에서 정의 CompletableFuture <T>들만 메소드 리턴 CompletionStage타입 T의 인수로하는 기능을 필요로 임의로 (모든 종류의) 결과를 생성 체인화 될 수
     * <U> CompletionStage<U> thenApply(Function<? super T, ? extends U> fn)
     * <U> CompletionStage<U> thenApplyAsync(Function<? super T,? extends U> fn)
     * <U> CompletionStage<U> thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)
     * CompletionStage<Void> thenAccept(Consumer<? super T> action)
     * CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action)
     * CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action, Executor executor)
     *
     * RESULT
     * 1.7320508075688772
     */
    @Test
    public void supplyExample2() {
        CompletableFuture.supplyAsync(() -> ThreadLocalRandom.current().nextInt(1, 10))
                .thenApply(Math::sqrt)
                .thenAccept(System.out::println)
                .join();
    }


    @Test
    public void suuplyExample3() {
        CompletableFuture.supplyAsync(() -> ThreadLocalRandom.current().nextInt(1, 10))
                .thenAccept(System.out::println)
                .join();
    }

    /**
     * 체인에 대한 매우 간단한 규칙이 있습니다
     * 를 반환 CompletionStage<Void>하거나 CompletableFuture<Void>연결할 수있는 메서드는 Runnable
     * 반환 방법이 CompletionStage<U>걸리는 방법과 체인 될 수있다 Consumer<U>또는 Function<U,R>
     *
     * 다음은 웹 페이지의 내용을 읽는 또 다른 예입니다
     *
     * RESULT
     * <!doctype html>
     * <html>
     * <head>
     *     <title>Example Domain</title>
     *
     *     <meta charset="utf-8" />
     *     <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
     *     <meta name="viewport" content="width=device-width, initial-scale=1" />
     *   .........
     * </head>
     *
     * <body>
     * <div>
     *     <h1>Example Domain</h1>
     *     <p>This domain is established to be used for illustrative examples in documents. You may use this
     *     domain in examples without prior coordination or asking for permission.</p>
     *     <p><a href="http://www.iana.org/domains/example">More information...</a></p>
     * </div>
     * </body>
     * </html>
     *
     * Task finished
     */
    @Test
    public void supplyExample4() {
        CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL("https://www.example.com/");
                try (InputStream is = url.openStream()) {
                    return new String(is.readAllBytes());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).thenAccept(System.out::println)
                .thenRun(() -> System.out.println("Task finished"))
                .join();
    }

    /**
     * 요약
     * 정적 팩토리 메소드에 의해 작성된 첫 번째 태스크 CompletableFuture는 Runnable또는 로 모델링됩니다 Supplier
     * 의 경우 Runnable후속 완료 단계도 있습니다 Runnable
     * 의 경우에 Supplier, 후속 단계의 완료는 하나 일 수 Consumer또는 Function타겟 스테이지 값을 반환한다 아닌지 여부에 따라
     * 또한을 포함하는 단계는을 포함하는 단계와 Consumer<T>더 연결될 수 있습니다 Runnable
     * 중간 완료 단계는 다음과 같이 요약 할 수 있습니다
     *
     * 스테이지 매개 변수 T를 사용 안함
     * Returns void: Runnable
     * Returns R: No supported method provided in CompletionStage
     *
     * 스테이지 매개 변수 T를 사용
     * Returns void: Consumer<T>
     * Returns R: Function<T, R>
     */
}
