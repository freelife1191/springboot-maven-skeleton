package com.java.async.dzone;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * 20. 실생활의 예
 * https://dzone.com/articles/20-examples-of-using-javas-completablefuture
 *
 * 이제 기능 CompletionStage과 구체적 CompletableFuture으로 살펴 보았으므로 아래 예제는 실제 시나리오에서 적용됩니다
 *
 * 1. 먼저 메소드 Car를 호출하여 객체 목록을 비동기 적으로 가져옵니다. 이 cars()메소드 는를 반환합니다 CompletionStage<List>
 *    이 cars()메소드는 백그라운드에서 원격 REST 엔드 포인트를 소비 할 수 있습니다
 *
 * 2. 그런 다음 자동차 등급을 비동기식으로 가져 오는 메소드를 CompletionStage<List>호출하여 (각각 REST 끝점을 소비 할 수 있음)
 *    rating(manufacturerId)메소드를 호출하여 각 자동차의 등급을 채우는 다른 것을 작성합니다 CompletionStage
 *
 * 3. 모든 Car객체가 등급으로 채워 지면 결국으로 끝나 List<CompletionStage>므로
 *    모든 단계가 완료되면 완료 allOf()되는 최종 단계 (variable에 저장 됨 done) 를 얻습니다
 *
 * 4. 사용 whenComplete()마지막 단계에, 우리는 인쇄 Car자신의 등급 개체를
 *
 * 때문에 Car인스턴스가 모두 독립적, 비동기 적으로 각 등급에 점점 성능이 향상됩니다
 * 또한 모든 자동차 등급이 채워질 allOf()때까지 기다리는 것은 수동 스레드 대기 (예 : Thread#join()또는 a CountDownLatch) 와 달리 보다 자연스러운 방법을 사용하여 수행됩니다
 *
 * 이 예제를 통해 작업하면이 API를 더 잘 이해하는 데 도움이됩니다. 이 예제의 전체 코드는 GitHub 에서 찾을 수 있습니다
 *
 * https://github.com/manouti/completablefuture-examples/blob/master/src/main/java/com/example/completablefuture/RealLifeWithoutCompletableFutureExample.java
 *
 * Created by KMS on 17/04/2020.
 */
public class RealLifeCompletableFutureExample {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        cars().thenCompose(cars -> {
            List<CompletionStage<Car>> updatedCars = cars.stream()
                    .map(car -> rating(car.manufacturerId).thenApply(r -> {
                        car.setRating(r);
                        return car;
                    })).collect(Collectors.toList());

            CompletableFuture<Void> done = CompletableFuture
                    .allOf(updatedCars.toArray(new CompletableFuture[updatedCars.size()]));
            return done.thenApply(v -> updatedCars.stream().map(CompletionStage::toCompletableFuture)
                    .map(CompletableFuture::join).collect(Collectors.toList()));
        }).whenComplete((cars, th) -> {
            if (th == null) {
                cars.forEach(System.out::println);
            } else {
                throw new RuntimeException(th);
            }
        }).toCompletableFuture().join();

        long end = System.currentTimeMillis();

        System.out.println("Took " + (end - start) + " ms.");
    }

    static CompletionStage<Float> rating(int manufacturer) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                simulateDelay();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            switch (manufacturer) {
                case 2:
                    return 4f;
                case 3:
                    return 4.1f;
                case 7:
                    return 4.2f;
                default:
                    return 5f;
            }
        }).exceptionally(th -> -1f);
    }

    static CompletionStage<List<Car>> cars() {
        List<Car> carList = new ArrayList<>();
        carList.add(new Car(1, 3, "Fiesta", 2017));
        carList.add(new Car(2, 7, "Camry", 2014));
        carList.add(new Car(3, 2, "M2", 2008));
        return CompletableFuture.supplyAsync(() -> carList);
    }

    private static void simulateDelay() throws InterruptedException {
        Thread.sleep(5000);
    }
}