package com.project.api.sample.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 비동기 처리 메인 서비스
 * 모든 비동기 처리 서비스는 이 서비스를 통해 서비스 한다
 * Created by KMS on 17/04/2020.
 */
@Service
public class AsyncService {

    /**
     * 비동기 처리 메인 메서드
     * @param runnable
     */
    @Async
    public void run(Runnable runnable) {
        runnable.run();
    }
}
