package com.project.api.sample.service;


import com.project.api.sample.repository.SampleApiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * https://jeong-pro.tistory.com/m/187
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SampleApiService {

    private final SampleApiRepository repository ;

    private final AsyncService asyncService;

    private final Executor executor;

    /**
     * 비동기 성공 서비스
     * @param i
     */
    @Async
    public void asyncTestSuccess(int i) {
        log.info("Async i = "+i);
    }

    /**
     * 비동기 실패 서비스
     * 내부서비스에서 @Async 서비스를 재 호출 하면 비동기 적용이 되지 않는다
     * https://dzone.com/articles/effective-advice-on-spring-async-part-1
     * @param i
     */
    public void asyncTestFail(int i) {
        asyncTestSuccess(i);
    }

    public void asyncTest(int i) {
        log.info("Async i = "+i);
    }

    /**
     * AsyncService 를 사용한 비동기 서비스
     * 내부 서비스 호출과 관계 없이 언제든지 서비스 호출 시 asyncService 의 run 메서드를 호출해
     * @Async 를 적용하여 사용할 수 있다
     * @param i
     */
    public void asyncTestAsyncService(int i) {
        asyncService.run(() -> asyncTest(i));
    }

    /**
     * Executor을 주입 받아 ThreadPool을 생성해서 비동기 처리
     * CompletableFuture 를 사용하여 비동기 서비스를 수행한다
     * @param i
     */
    public void asyncTestCompletableFuture(int i) {
        CompletableFuture.runAsync(() -> asyncTest(i), executor);
    }

}
