package com.java.async.dzone;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Java CompletableFuture 20 가지 사용 예
 * https://dzone.com/articles/20-examples-of-using-javas-completablefuture
 *
 * 이 게시물은 Java 8의 CompletionStageAPI와 특히 표준 Java 라이브러리에서의 구현을 다시 살펴  CompletableFuture 봅니다
 * API는 다양한 동작을 설명하는 예제로 설명되며 각 예제는 하나 또는 두 개의 특정 동작에 중점을 둡니다
 *
 * CompletableFuture클래스가 CompletionStage인터페이스를 구현 하므로 먼저 해당 인터페이스의 계약을 이해해야합니다
 * 동기식 또는 비동기식으로 수행 할 수있는 특정 계산 단계를 나타냅니다
 * 이를 최종 계산 결과를 생성하는 계산 파이프 라인의 단일 단위로 생각할 수 있습니다
 * 여러 가지 것을이 수단 CompletionStage의이 그렇게 한 단계의 완료가 차례로 다른 트리거 다른 단계의 실행을 유발한다는 등 서로 연결 할 수 있습니다
 *
 * CompletionStage인터페이스 를 구현하는 것 외에도이 미래 를 명시 적으로 완료 할 수있는 보류중인 비동기 이벤트를 나타내는을
 * CompletableFuture 구현 하므로 CompletableFuture라는 이름이 사용됩니다 Future
 * Created by KMS on 16/04/2020.
 */
public class CompletableFutureExamples {

    static Random random = new Random();

    /**
     * 1. Creating a Completed CompletableFuture
     * 가장 간단한 예는 미리 정의 된 결과로 이미 완료된 CompletableFuture를 만듭니다
     * 일반적으로 이것은 계산의 시작 단계로 작용할 수 있습니다
     *
     * getNow (null)은 완료된 경우 결과를 반환하지만 (그렇지 않은 경우) null (인수)을 반환합니다
     */
    @Test
    public void completedFutureExample() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message");
        assertTrue(cf.isDone());
        assertEquals("message", cf.getNow(null));
    }

    /**
     * 2. 간단한 비동기 스테이지 실행
     * 다음 예제는 Runnable 비동기 적으로 실행되는 스테이지를 만드는 방법입니다
     *
     * 이 예제의 요약은 두 가지입니다
     * 1. 메서드가 일반적으로 키워드로 끝나면 CompletableFuture가 비동기 적으로 실행됩니다. Async
     * 2. 기본적으로 (아니오 Executor를 지정하면) 비동기 실행 ForkJoinPool은 데몬 스레드를 사용 하여 Runnable작업 을 실행하는 공통 구현을 사용합니다
     * 이는 특정 CompletableFuture입니다
     * 다른 CompletionStage구현은 기본 동작을 무시할 수 있습니다
     */
    @Test
    public void runAsyncExample() {
        CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
            assertTrue(Thread.currentThread().isDaemon());
            randomSleep();
        });
        assertFalse(cf.isDone());
        sleepEnough();
        assertTrue(cf.isDone());
    }

    /**
     * 3. 이전 단계에서 기능 적용
     * 아래 예제는 CompletableFuture예제 #1에서 완성 된 결과 문자열을 가져 와서 결과 "message" 를 대문자로 변환하는 함수를 적용합니다
     *
     * 의 행동 키워드를 참고하십시오 thenApply:
     *
     * 1. then 은 현재 단계가 정상적으로 완료 될 때 (예외없이)이 단계의 조치가 발생 함을 의미합니다
     * 이 경우 현재 단계는 이미 "message"값으로 완료됩니다
     * 2. Apply 는 반환 된 스테이지가 Function이전 스테이지의 결과에 를 적용 함을 의미합니다
     *
     * 의 실행이 Function차단되므로 대문자 작업이 완료된 경우에만 getNow()에 도달합니다
     */
    @Test
    public void thenApplyExample() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApply(s -> {
            assertFalse(Thread.currentThread().isDaemon());
            return s.toUpperCase();
        });
        assertEquals("MESSAGE", cf.getNow(null));
    }

    /**
     * 4. Function이전 단계에서 비동기 적으로 적용
     * Async앞의 예제에서 메소드에 접미사를 추가하면 체인 CompletableFuture이 비동기식으로 실행됩니다 (를 사용하여 ForkJoinPool.commonPool())
     */
    @Test
    public void thenApplyAsyncExample() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(s -> {
            assertTrue(Thread.currentThread().isDaemon());
            randomSleep();
            return s.toUpperCase();
        });
        assertNull(cf.getNow(null));
        assertEquals("MESSAGE", cf.join());
    }


    private ExecutorService executor = Executors.newFixedThreadPool(3, new ThreadFactory() {
        int count = 1;
        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "custom-executor-" + count++);
        }
    });

    /**
     * 5. 사용자 정의 실행기를 사용하여 이전 단계에서 함수를 비동기 적으로 적용
     * 비동기 메서드의 매우 유용한 기능은 Executor원하는 실행 방법을 제공하는 기능 CompletableFuture입니다
     * 이 예제는 고정 스레드 풀을 사용하여 대문자 변환을 적용하는 방법을 보여줍니다 Function
     */
    @Test
    public void thenApplyAsyncWithExecutorExample() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(s -> {
            assertTrue(Thread.currentThread().getName().startsWith("custom-executor-"));
            assertFalse(Thread.currentThread().isDaemon());
            randomSleep();
            return s.toUpperCase();
        }, executor);
        assertNull(cf.getNow(null));
        assertEquals("MESSAGE", cf.join());
    }

    /**
     * 6. 이전 단계의 결과 소비
     * 다음 단계가 현재 단계의 결과를 수락하지만 계산에서 값을 반환 할 필요가없는 경우 (즉, 반환 유형이 void 임)을 적용하는 대신을
     * Function수락 할 수 Consumer있으므로 메소드는 thenAccept다음과 같습니다
     *
     * 은 Consumer우리가 반환에 가입 할 필요가 없습니다, 동 기적으로 실행됩니다 CompletableFuture
     */
    @Test
    public void thenAcceptExample() {
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture("thenAccept message")
                .thenAccept(s -> result.append(s));
        assertTrue(result.length() > 0, "Result was empty");
    }

    /**
     * 7. 이전 단계의 결과를 비동기 적으로 소비
     * 다시 한 번 비동기 버전을 사용하면 thenAccept체인 CompletableFuture이 비동기 적으로 실행됩니다
     */
    @Test
    public void thenAcceptAsyncExample() {
        StringBuilder result = new StringBuilder();
        CompletableFuture<Void> cf = CompletableFuture.completedFuture("thenAcceptAsync message")
                .thenAcceptAsync(s -> result.append(s));
        cf.join();
        assertTrue(result.length() > 0, "Result was empty");
    }

    /**
     * 8. 예외적으로 계산 완료
     * 이제 비동기 연산을 예외적 으로 명시 적으로 완료 하여 계산 실패를 나타내는 방법을 살펴 보겠습니다
     * 간단히하기 위해 연산은 문자열을 가져와 대문자로 변환하고 1 초의 연산 지연을 시뮬레이션합니다
     * 이를 위해 thenApplyAsync(Function, Executor)첫 번째 인수가 대문자 함수 인 메소드 를 사용합니다
     * 실행기는 실제로 실행을 common에 제출하기 전에 1 초 동안 대기 하는 지연된 실행기 입니다 ForkJoinPool
     *
     * 이 예제를 자세히 살펴 보자
     *
     * 먼저, CompletableFuture값으로 이미 완료된를 만듭니다 "message"
     * 다음으로을 호출 thenApplyAsync하면 new가 반환 CompletableFuture됩니다
     * 이 방법은 첫 번째 단계가 완료되면 비동기 방식으로 대문자 변환을 적용합니다 (이미 완료되었으므로 Function즉시 실행됩니다)
     * 이 예제는 또한 delayedExecutor(timeout, timeUnit)메소드를 사용하여 비동기 태스크를 지연시키는 방법을 보여줍니다
     *
     * 그런 다음 exceptionHandler다른 메시지를 반환하여 예외를 처리 하는 별도의 "처리기"단계를 만듭니다 "message upon cancel"
     *
     * 다음으로 예외적으로 두 번째 단계를 명시 적으로 완료합니다
     * 이렇게하면 join()대문자 작업을 수행하는 스테이지 의 메서드가 a를 던집니다 CompletionException(일반적으로 join()대문자 문자열을 얻기 위해 1 초 동안 기다렸을 것입니다)
     * 또한 핸들러 스테이지를 트리거합니다
     */
    @Test
    public void completeExceptionallyExample() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(String::toUpperCase,
                CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS));
        CompletableFuture<String> exceptionHandler = cf.handle((s, th) -> { return (th != null) ? "message upon cancel" : ""; });
        cf.completeExceptionally(new RuntimeException("completed exceptionally"));
        assertTrue(cf.isCompletedExceptionally(), "Was not completed exceptionally");
        try {
            cf.join();
            fail("Should have thrown an exception");
        } catch(CompletionException ex) { // just for testing
            assertEquals("completed exceptionally", ex.getCause().getMessage());
        }
        assertEquals("message upon cancel", exceptionHandler.join());
    }

    /**
     * 9. 계산 취소
     * 예외적 인 완료에 매우 가까워 인터페이스 에서 cancel(boolean mayInterruptIfRunning)메소드를 통해 계산을 취소 할 수 있습니다 Future
     * 의 경우 CompletableFuture구현시 취소를 수행하기 위해 인터럽트를 사용하지 않으므로 부울 매개 변수는 사용되지 않습니다
     * 대신에 cancel()해당합니다 completeExceptionally(new CancellationException())
     */
    @Test
    public void cancelExample() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message").thenApplyAsync(String::toUpperCase,
                CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS));
        CompletableFuture<String> cf2 = cf.exceptionally(throwable -> "canceled message");
        assertTrue(cf.cancel(true), "Was not canceled");
        assertTrue(cf.isCompletedExceptionally(), "Was not completed exceptionally");
        assertEquals("canceled message", cf2.join());
    }

    /**
     * 10. 완성 된 두 단계의 결과에 기능 적용
     * 아래 예제는 이전 두 단계 중 하나의 결과에 a CompletableFuture를 적용 하는를 만듭니다 Function(어느 하나가에 전달 될지는 보장하지 않습니다 Function)
     * 문제의 두 단계는 다음과 같습니다
     * 하나는 원래 문자열에 대문자 변환을 적용하고 다른 하나는 소문자 변환을 적용합니다
     */
    @Test
    public void applyToEitherExample() {
        String original = "Message";
        CompletableFuture<String> cf1 = CompletableFuture.completedFuture(original)
                .thenApplyAsync(s -> delayedUpperCase(s));
        CompletableFuture<String> cf2 = cf1.applyToEither(
                CompletableFuture.completedFuture(original).thenApplyAsync(s -> delayedLowerCase(s)),
                s -> s + " from applyToEither");
        assertTrue(cf2.join().endsWith(" from applyToEither"));
    }

    /**
     * 11. 두 개의 완료된 단계 중 하나의 결과 소비
     * 앞의 예와 비슷하지만 a Consumer대신에 Function(를 사용하는 CompletableFuture경우 void 유형이 있습니다)
     *
     * StringBuffer 여기서 스레드 안전을 대신 사용하십시오
     * StringBuilder
     */
    @Test
    public void acceptEitherExample() {
        String original = "Message";
        StringBuffer result = new StringBuffer();
        CompletableFuture<Void> cf = CompletableFuture.completedFuture(original)
                .thenApplyAsync(s -> delayedUpperCase(s))
                .acceptEither(CompletableFuture.completedFuture(original).thenApplyAsync(s -> delayedLowerCase(s)),
                        s -> result.append(s).append("acceptEither"));
        cf.join();
        assertTrue(result.toString().endsWith("acceptEither"), "Result was empty");
    }

    /**
     * 12. 두 단계가 완료되면 실행 가능 파일 실행
     * 이 예는 두 단계 모두 완료시 트리거 CompletableFuture를 실행하는 종속 장치 가 어떻게 Runnable트리거 되는지 보여줍니다
     * 아래의 모든 단계는 동 기적으로 실행됩니다
     * 여기서 스테이지는 먼저 메시지 문자열을 대문자로 변환 한 다음 두 번째는 동일한 메시지 문자열을 소문자로 변환합니다
     */
    @Test
    public void runAfterBothExample() {
        String original = "Message";
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture(original).thenApply(String::toUpperCase).runAfterBoth(
                CompletableFuture.completedFuture(original).thenApply(String::toLowerCase),
                () -> result.append("done"));
        assertTrue(result.length() > 0, "Result was empty");
    }

    /**
     * 13. BiConsumer에서 두 단계의 결과 수락
     * Runnable두 단계를 모두 완료 하면를 실행하는 대신 BiConsumer필요한 경우를 사용하면 필요한 경우 결과를 처리 할 수 있습니다
     */
    @Test
    public void thenAcceptBothExample() {
        String original = "Message";
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture(original).thenApply(String::toUpperCase).thenAcceptBoth(
                CompletableFuture.completedFuture(original).thenApply(String::toLowerCase),
                (s1, s2) -> result.append(s1 + s2));
        assertEquals("MESSAGEmessage", result.toString());
    }

    /**
     * 14. 두 단계의 결과에 BiFunction 적용
     * 종속자가 CompletableFuture이전에 두 개의 결과를 결합하여 CompletableFuture함수를 적용하고 결과를 반환하려는 경우 메소드를 사용할 수 있습니다 thenCombine()
     * 전체 파이프 라인은 동기식이므로 getNow()최종 결과는 대문자와 소문자 결과를 연결하는 최종 결과를 검색합니다
     */
    @Test
    public void thenCombineExample() {
        String original = "Message";
        CompletableFuture<String> cf = CompletableFuture.completedFuture(original).thenApply(s -> delayedUpperCase(s))
                .thenCombine(CompletableFuture.completedFuture(original).thenApply(s -> delayedLowerCase(s)),
                        (s1, s2) -> s1 + s2);
        assertEquals("MESSAGEmessage", cf.getNow(null));
    }

    /**
     * 15. 두 단계의 결과에 BiFunction을 비동기 적으로 적용
     * 이전 예제와 비슷하지만 동작이 CompletableFuture다릅니다
     * 두 단계가 모두 비동기 적으로 thenCombine()실행되므로 이 방법은 Async접미사 가없는 경우에도 비동기 적으로 실행 됩니다
     * 이것은 Javadocs 클래스에 문서화되어 있습니다
     * 따라서 결과를 기다리 join()려면 결합 해야합니다 CompletableFuture
     */
    @Test
    public void thenCombineAsyncExample() {
        String original = "Message";
        CompletableFuture<String> cf = CompletableFuture.completedFuture(original)
                .thenApplyAsync(s -> delayedUpperCase(s))
                .thenCombine(CompletableFuture.completedFuture(original).thenApplyAsync(s -> delayedLowerCase(s)),
                        (s1, s2) -> s1 + s2);
        assertEquals("MESSAGEmessage", cf.join());
    }

    /**
     * 16. CompletableFutures 작성
     * 컴포지션 사용 thenCompose()을 사용 하여 이전 두 예제에서 수행 한 것과 동일한 계산을 수행 할 수 있습니다
     * 이 방법은 첫 번째 단계 (대문자 변환 적용)가 완료되기를 기다립니다
     * 결과는 지정된로 전달되어을 Function반환하며 CompletableFuture, 결과는 반환 된 결과입니다 CompletableFuture
     * 이 경우 함수는 대문자 문자열 ( upper)을 가져 와서 문자열을 소문자 CompletableFuture로 변환 original한 다음에 추가합니다 upper
     */
    @Test
    public void thenComposeExample() {
        String original = "Message";
        CompletableFuture<String> cf = CompletableFuture.completedFuture(original).thenApply(s -> delayedUpperCase(s))
                .thenCompose(upper -> CompletableFuture.completedFuture(original).thenApply(s -> delayedLowerCase(s))
                        .thenApply(s -> upper + s));
        assertEquals("MESSAGEmessage", cf.join());
    }

    /**
     * 17. 여러 단계 중 하나가 완료되면 완료되는 단계 작성
     * 아래 예제 CompletableFuture는 여러 CompletableFuture결과 중 하나 가 완료되면 동일한 결과로 완료 되는 것을 만드는 방법을 보여줍니다
     * 문자열을 목록에서 대문자로 각각 변환하는 여러 단계가 먼저 작성됩니다
     * 이 모든 CompletableFuture들이 (을 사용하여 thenApply()) 동 기적으로 실행 되기 때문에, 호출 될 때마다 모든 단계가 완료되기 때문에 CompletableFuture로부터 리턴 된 anyOf()것이 즉시 실행됩니다
     * 그런 다음을 사용하여 whenComplete(BiConsumer<? super Object, ? super Throwable> action)결과를 처리합니다 (결과가 대문자인지 확인)
     */
    @Test
    public void anyOfExample() {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture<String>> futures = messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApply(s -> delayedUpperCase(s)))
                .collect(Collectors.toList());
        CompletableFuture.anyOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete((res, th) -> {
            if(th == null) {
                assertTrue(isUpperCase((String) res));
                result.append(res);
            }
        });
        assertTrue(result.length() > 0, "Result was empty");
    }

    /**
     * 18. 모든 단계가 완료되면 완료되는 단계 작성
     * 다음 두 예 CompletableFuture는 각각 여러 가지가 모두 완료되면 CompletableFuture동기 방식과 비동기 방식으로 완료 되는 완성형 을 만드는 방법을 보여줍니다
     * 시나리오는 이전 예제와 동일합니다
     * 각 요소가 대문자로 변환되는 문자열 목록이 제공됩니다
     */
    @Test
    public void allOfExample() {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture<String>> futures = messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApply(s -> delayedUpperCase(s)))
                .collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete((v, th) -> {
            futures.forEach(cf -> assertTrue(isUpperCase(cf.getNow(null))));
            result.append("done");
        });
        assertTrue(result.length() > 0, "Result was empty");
    }

    /**
     * 19. 모든 단계가 완료되면 비동기식으로 완료되는 단계 작성
     * thenApplyAsync()개별에서 전환 CompletableFuture하면 스테이지 allOf()가 완료 한 공통 풀 스레드 중 하나가 리턴 한 스테이지 가 실행됩니다
     * 따라서 join()완료 될 때까지 기다려야합니다
     */
    @Test
    public void allOfAsyncExample() {
        StringBuilder result = new StringBuilder();
        List<String> messages = Arrays.asList("a", "b", "c");
        List<CompletableFuture<String>> futures = messages.stream()
                .map(msg -> CompletableFuture.completedFuture(msg).thenApplyAsync(s -> delayedUpperCase(s)))
                .collect(Collectors.toList());
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .whenComplete((v, th) -> {
                    futures.forEach(cf -> assertTrue(isUpperCase(cf.getNow(null))));
                    result.append("done");
                });
        allOf.join();
        assertTrue(result.length() > 0, "Result was empty");
    }

    private boolean isUpperCase(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLowerCase(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private String delayedUpperCase(String s) {
        randomSleep();
        return s.toUpperCase();
    }

    private String delayedLowerCase(String s) {
        randomSleep();
        return s.toLowerCase();
    }

    private void randomSleep() {
        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            // ...
        }
    }

    private void sleepEnough() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // ...
        }
    }

}
