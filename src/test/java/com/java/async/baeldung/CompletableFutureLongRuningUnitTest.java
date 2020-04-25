package com.java.async.baeldung;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * GUIDE
 * https://www.baeldung.com/java-completablefuture
 * SOURCE CODE
 * https://github.com/eugenp/tutorials/blob/master/core-java-modules/core-java-concurrency-basic/src/test/java/com/baeldung/completablefuture/CompletableFutureLongRunningUnitTest.java
 * Created by KMS on 16/04/2020.
 */
@Slf4j
public class CompletableFutureLongRuningUnitTest {

    /******************************** 1. 간단한 Future로 CompletableFuture 사용 *************************************/

    /**
     * Future 객체로 반환 받는 메서드를 통해 비동기로 수행하고
     * complete 메서드로 완료
     * 수행한 비동기 메서드가 완료될 때까지 get 메서드를 사용하여 대기함
     *
     * 계산을 시작하기 위해 "Java의 스레드 풀 소개"기사에 설명 된 Executor API를 사용합니다.
     * 그러나 CompletableFuture를 작성하고 완료하는이 방법은 원시 스레드를 포함한 모든 동시성 메커니즘 또는 API와 함께 사용할 수 있습니다
     * calculateAsync 메소드는 Future 인스턴스를 리턴합니다
     * 우리는 단순히 메소드를 호출하고 Future 인스턴스를 수신하고 결과를 차단할 준비가되면 get 메소드를 호출합니다
     * 또한 get 메소드는 확인 된 예외를 발생시킵니다
     * 즉 ExecutionException (계산 중 발생한 예외를 캡슐화 함) 및 InterruptedException (메소드를 실행하는 스레드가 중단되었음을 나타내는 예외)입니다
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void whenRunningCompletableFutureAsynchronously_thenGetMethodWaitsForResult() throws InterruptedException, ExecutionException {
        Future<String> completableFuture = calculateAsync();

        String result = completableFuture.get();
        assertEquals("Hello", result);
    }

    /**
     * 우선 CompletableFuture 클래스는 Future 인터페이스를 구현 하므로 이를 추가 구현 로직과 함께 Future 구현 으로 사용할 수 있습니다
     * 예를 들어 인수가없는 생성자를 사용하여이 클래스의 인스턴스를 만들어 미래의 결과를 나타내고 소비자에게 건네주고 나중에 complete 메소드를 사용하여 완료 할 수 있습니다
     * 소비자는 이 메소드가 제공 될 때까지 get 메소드를 사용하여 현재 스레드를 차단할 수 있습니다
     * 아래 예제에는 CompletableFuture 인스턴스를 만든 다음 다른 스레드에서 일부 계산을 스핀하고 Future를 즉시 반환 하는 메서드가 있습니다
     * 계산이 완료 되면 메소드 는 결과를 complete 메소드 에 제공하여 Future 를 완료 합니다
     * @return
     * @throws InterruptedException
     */
    private Future<String> calculateAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool()
                .submit(() -> {
                    Thread.sleep(500);
                    completableFuture.complete("Hello");
                    return null;
                });

        return completableFuture;
    }

    /**
     * 계산 결과를 이미 알고있는 경우이 계산 결과를 나타내는 인수와 함께 static completedFuture 메서드를 사용할 수 있습니다
     * 그러면 미래의 get 메소드가 차단되지 않고 대신 즉시이 결과를 리턴합니다
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void whenRunningCompletableFutureWithResult_thenGetMethodReturnsImmediately() throws InterruptedException, ExecutionException {
        Future<String> completableFuture = CompletableFuture.completedFuture("Hello");

        String result = completableFuture.get();
        assertEquals("Hello", result);
    }

    /**
     * 대안 시나리오로서 Future의 실행을 취소 할 수 있습니다
     * 결과를 찾지 못하고 비동기 실행을 모두 취소하기로 결정했다고 가정하십시오
     * 이는 Future의 cancel 메소드로 수행 할 수 있습니다
     * 이 메소드는 부울 인수 mayInterruptIfRunning을 수신합니다
     * 그러나 CompletableFuture의 경우 인터럽트가 CompletableFuture의 처리를 제어하는데 사용되지 않으므로 효과가 없습니다.
     * 비동기 메소드의 수정 된 버전은 다음과 같습니다
     * @return
     * @throws InterruptedException
     */
    private Future<String> calculateAsyncWithCancellation() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool()
                .submit(() -> {
                    Thread.sleep(500);
                    completableFuture.cancel(false);
                    return null;
                });

        return completableFuture;
    }

    /**
     * Future.get() 메소드를 사용하여 결과를 차단하면 미래가 취소되면 CancellationException이 발생합니다
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test(expected = CancellationException.class)
    public void whenCancelingTheFuture_thenThrowsCancellationException() throws ExecutionException, InterruptedException {
        Future<String> future = calculateAsyncWithCancellation();
        future.get();
    }

    /**
     * 4. 캡슐화 된 계산 로직을 갖춘 CompletableFuture
     * runAsync 및 supplyAsync 를 사용하면 Runnable 및 Supplier 기능 유형으로 CompletableFuture 인스턴스를 작성
     * Runnable과 Supplier는 새로운 Java 8 기능 덕분에 인스턴스를 람다 식으로 전달할 수있는 기능적 인터페이스입니다
     * Runnable 인터페이스는 스레드에서 사용되는 것과 동일한 이전 인터페이스이며 값을 반환 할 수 없습니다
     * 공급 업체 인터페이스는 인수가없고 매개 변수화 된 유형의 값을 리턴하는 단일 메소드가있는 일반 기능 인터페이스입니다
     * 이를 통해 계산을 수행하고 결과를 반환하는 람다 식으로 공급 업체 인스턴스를 제공 할 수 있습니다
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void whenCreatingCompletableFutureWithSupplyAsync_thenFutureReturnsValue() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");

        assertEquals("Hello", future.get());
    }

    /****************************** 5. 비동기 계산 결과 처리 *************************************/

    /**
     * 계산 결과를 처리하는 가장 일반적인 방법은 이를 계산하는 것입니다
     * thenApply 메소드는 정확히 다음을 수행합니다
     * Function 인스턴스를 승인합니다
     * 이를 사용하여 결과를 처리하고 함수가 반환 한 값을 보유하는 Future를 반환합니다
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void whenAddingThenApplyToFuture_thenFunctionExecutesAfterComputationIsFinished() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

        CompletableFuture<String> future = completableFuture.thenApply(s -> s + " World");

        assertEquals("Hello World", future.get());
    }

    /**
     * 계산 결과를 처리하는 가장 일반적인 방법은이를 계산하는 것입니다
     * thenApply 메소드는 정확히 다음을 수행합니다
     * Function 인스턴스를 승인합니다
     * 이를 사용하여 결과를 처리하고 함수가 반환한 값을 보유하는 Future를 반환합니다
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void whenAddingThenAcceptToFuture_thenFunctionExecutesAfterComputationIsFinished() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

        CompletableFuture<Void> future = completableFuture.thenAccept(s -> log.debug("Computation returned: " + s));

        future.get();
    }

    /**
     * 마지막으로 계산 값이 필요하지 않거나 체인의 끝에서 일부 값을 반환하려는 경우 Runnable 람다를 thenRun 메서드에 전달할 수 있습니다
     * 다음 예제에서 future.get() 메소드가 호출 된 후 콘솔에 단순히 라인을 인쇄합니다
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void whenAddingThenRunToFuture_thenFunctionExecutesAfterComputationIsFinished() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

        CompletableFuture<Void> future = completableFuture.thenRun(() -> log.debug("Computation finished."));

        future.get();
    }

    /************************************* 6. Combining Futures *******************************************/

    /**
     * CompletableFuture API의 가장 중요한 부분은 CompletableFuture 인스턴스를 일련의 계산 단계로 결합하는 기능입니다
     * 이 연결의 결과는 그 자체로 추가 연결 및 결합을 허용하는 CompletableFuture입니다
     * 이 접근법은 기능적 언어에서 어디에나 존재하며 종종 모나 딕 디자인 패턴이라고 합니다
     * 다음 예제에서는 thenCompose 메소드를 사용하여 두 선물을 순차적으로 연결합니다
     * 이 메서드는 CompletableFuture 인스턴스를 반환하는 함수를 사용합니다
     * 이 함수의 인수는 이전 계산 단계의 결과입니다
     * 이를 통해 다음 CompletableFuture의 람다 내에서이 값을 사용할 수 있습니다
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void whenUsingThenCompose_thenFuturesExecuteSequentially() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello")
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));

        assertEquals("Hello World", completableFuture.get());
    }

    /**
     * thenCompose와 thenCompose 메소드는 모나 딕 패턴의 기본 빌딩 블록을 구현합니다
     * Java 8에서도 사용 가능한 Stream 및 Optional 클래스의 map 및 flatMap 메소드와 밀접한 관련이 있습니다
     * 두 메소드 모두 함수를 수신하여이를 계산 결과에 적용하지만 thenCompose (flatMap) 메소드는 동일한 유형의 다른 오브젝트를 리턴하는 함수를 수신합니다
     * 이 기능 구조를 통해 이러한 클래스의 인스턴스를 빌딩 블록으로 구성 할 수 있습니다
     * 두 개의 독립적 인 선물을 실행하고 그 결과로 무언가를 수행하려면 두 개의 인수가있는 미래와 함수를 허용하는 thenCombine 메소드를 사용하여 두 결과를 처리하십시오
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void whenUsingThenCombine_thenWaitForExecutionOfBothFutures() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello")
                .thenCombine(CompletableFuture.supplyAsync(() -> " World"), (s1, s2) -> s1 + s2);

        assertEquals("Hello World", completableFuture.get());
    }

    /**
     * 더 간단한 경우는 두 가지 선물 '결과로 무언가를하고 싶지만 미래의 가치를 전달할 필요는 없습니다. thenAcceptBoth 메소드가 도움이됩니다
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void whenUsingThenAcceptBoth_thenWaitForExecutionOfBothFutures() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(() -> "Hello")
                .thenAcceptBoth(CompletableFuture.supplyAsync(() -> " World"), (s1, s2) -> log.debug(s1 + s2));
    }

    /*********************************** 7. thenApply ()와 thenCompose ()의 차이점 *****************************************/
    /**
     * 이전 섹션에서 thenApply() 및 thenCompose()에 대한 예제를 보여주었습니다
     * 두 API 모두 서로 다른 CompletableFuture 호출을 연결하는 데 도움이되지만이 두 기능의 사용법은 다릅니다
     */

    public CompletableFuture<Integer> compute(){
        return CompletableFuture.supplyAsync(() -> 10);
    }

    public CompletableFuture<Integer> computeAnother(Integer i){
        return CompletableFuture.supplyAsync(() -> 10 + i);
    }

    /**
     * 7.1. thenApply()
     * 이 방법은 이전 호출의 결과로 작업하는 데 사용됩니다
     * 그러나 기억해야 할 핵심 사항은 반환 유형이 모든 통화와 결합된다는 것입니다
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void whenPassingTransformation_thenFunctionExecutionWithThenApply() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> finalResult = compute().thenApply(s -> s + 1);
        assertTrue(finalResult.get() == 11);
    }

    /**
     * 7.2. thenCompose()
     * thenCompose() 메소드는 새로운 완료 단계를 리턴한다는 점에서 thenApply()와 유사합니다
     * 그러나 thenCompose()는 이전 단계를 인수로 사용합니다
     * thenApply()에서 관찰 한 것처럼 중첩 된 미래가 아니라 결과를 사용하여 미래를 평평하게하고 반환합니다
     *
     * 따라서 아이디어가 CompletableFuture 메소드를 연결하는 경우 thenCompose()를 사용하는 것이 좋습니다
     * 또한이 두 메소드의 차이점은 map()과 flatMap()의 차이점과 유사합니다
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void whenPassingPreviousStage_thenFunctionExecutionWithThenCompose() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> finalResult = compute().thenCompose(this::computeAnother);
        assertTrue(finalResult.get() == 20);
    }

    /************************************ 8. Running Multiple Futures in Parallel *****************************************/

    /**
     * 여러 Futures를 병렬로 실행해야하는 경우 일반적으로 모든 Futures가 실행될 때까지 기다렸다가 결합 된 결과를 처리하려고 합니다
     * CompletableFuture.allOf 정적 메소드를 사용하면 var-arg로 제공된 모든 Futures의 완료를 기다릴 수 있습니다
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void whenFutureCombinedWithAllOfCompletes_thenAllFuturesAreDone() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Beautiful");
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "World");

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2, future3);

        // ...

        combinedFuture.get();

        assertTrue(future1.isDone());
        assertTrue(future2.isDone());
        assertTrue(future3.isDone());

        /**
         * CompletableFuture.allOf()의 반환 유형은 CompletableFuture <Void>입니다
         * 이 방법의 한계는 모든 선물의 결합 된 결과를 반환하지 않는다는 것입니다
         * 대신 선물에서 수동으로 결과를 얻어야합니다
         * 다행히 CompletableFuture.join() 메소드와 Java 8 Streams API로 간단
         */
        String combined = Stream.of(future1, future2, future3)
                .map(CompletableFuture::join)
                .collect(Collectors.joining(" "));

        assertEquals("Hello Beautiful World", combined);

        /**
         * CompletableFuture.join() 메소드는 get 메소드와 유사하지만 Future가 정상적으로 완료되지 않은 경우 검사되지 않은 예외가 발생합니다
         * 이를 통해 Stream.map() 메서드에서 메서드 참조로 사용할 수 있습니다
         */
    }

    /****************************************** 9. Handling Errors **************************************************/

    /**
     * 일련의 비동기 계산 단계에서 오류 처리를하려면 throw / catch 관용구가 비슷한 방식으로 조정되어야 했습니다
     * 구문 블록에서 예외를 포착하는 대신 CompletableFuture 클래스를 사용하면 특수 핸들 메소드에서 예외를 처리 할 수 있습니다
     * 이 메소드는 계산 결과 (성공적으로 완료된 경우)와 예외 발생 (일부 계산 단계가 정상적으로 완료되지 않은 경우)의 두 매개 변수를 수신합니다
     * 다음 예제에서는 이름이 제공되지 않아 인사말의 비동기 계산이 오류로 완료된 경우 handle 메소드를 사용하여 기본값을 제공합니다
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void whenFutureThrows_thenHandleMethodReceivesException() throws ExecutionException, InterruptedException {
        String name = null;

        // ...

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            if (name == null) {
                throw new RuntimeException("Computation error!");
            }
            return "Hello, " + name;
        }).handle((s, t) -> s != null ? s : "Hello, Stranger!");

        assertEquals("Hello, Stranger!", completableFuture.get());
    }

    /**
     * 대안 시나리오로서, 첫 번째 예에서와 같이 미래를 수동으로 가치를 가지고 완성하고 예외와 함께 그것을 완성하는 능력을 갖기를 원한다고 가정하십시오
     * completeExceptionally 메소드는이를위한 것입니다
     * 다음 예제의 completableFuture.get() 메소드는 그 원인으로 RuntimeException이있는 ExecutionException을 발생시킵니다
     * 예제에서 handle 메소드를 사용하여 예외를 비동기 적으로 처리 할 수​있었지만 get 메소드를 사용하면보다 일반적인 동기식 예외 처리 방식을 사용할 수 있습니다
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test(expected = ExecutionException.class)
    public void whenCompletingFutureExceptionally_thenGetMethodThrows() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        // ...

        completableFuture.completeExceptionally(new RuntimeException("Calculation failed!"));

        // ...

        completableFuture.get();
    }

    /************************************* 10. Async Methods *****************************************/

    /**
     * CompletableFuture 클래스의 유창한 API의 대부분의 메소드에는 비동기 접미사가있는 두 가지 추가 변형이 있습니다
     * 이러한 메소드는 일반적으로 다른 스레드에서 해당 실행 단계를 실행하기위한 것입니다
     *
     * 비동기 postfix가없는 메소드는 호출 스레드를 사용하여 다음 실행 단계를 실행합니다
     * Executor 인수가없는 Async 메소드는 ForkJoinPool.commonPool() 메소드로 액세스되는 Executor의 공통 fork/join pool 구현을 사용하여 단계를 실행합니다
     * Executor 인수를 가진 Async 메소드는 전달 된 Executor를 사용하여 단계를 실행합니다
     *
     * 다음은 Function 인스턴스를 사용한 계산 결과를 처리하는 수정 된 예입니다
     * 눈에 띄는 차이점은 thenApplyAsync 메서드입니다
     * 그러나 기본적으로 함수의 응용 프로그램은 ForkJoinTask 인스턴스에 래핑됩니다 (fork/join 프레임 워크에 대한 자세한 내용은 "Java의 Fork/Join 프레임 워크 안내서" 기사 참조)
     * 이를 통해 계산을 훨씬 더 병렬화하고 시스템 리소스를보다 효율적으로 사용할 수 있습니다
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void whenAddingThenApplyAsyncToFuture_thenFunctionExecutesAfterComputationIsFinished() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

        CompletableFuture<String> future = completableFuture.thenApplyAsync(s -> s + " World");

        assertEquals("Hello World", future.get());
    }
}
