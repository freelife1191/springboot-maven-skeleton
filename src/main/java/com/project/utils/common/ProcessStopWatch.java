package com.project.utils.common;

import lombok.extern.slf4j.Slf4j;

/**
 * 프로세스 수행시간 측정
 * Created by KMS on 01/07/2020.
 */
@Slf4j
public class ProcessStopWatch {

    public static long tmpTime = 0L;
    public static String serviceMessage = "";

    /**
     * 프로세스 시작 처리
     * @param message
     */
    public static void start(String message) {
        serviceMessage = message;
        log.info("== {} 수행 시작 ==",serviceMessage);
        tmpTime = System.currentTimeMillis();
    }

    /**
     * 프로세스 종료 처리
     */
    public static void end() {
        if(tmpTime == 0L){
            log.info("프로세스 시작 처리부터 수행해 주세요");
            return;
        }
        log.info("== {} 수행 종료 :: 처리시간 : {} ==",serviceMessage,(System.currentTimeMillis() - tmpTime)/1000.0);

        // static 변수 초기화
        ProcessStopWatch.tmpTime = 0L;
        ProcessStopWatch.serviceMessage = "";
    }
}
