package com.project.api.sample.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * Sample API Repository
 */
@Repository
@Transactional
public class SampleApiRepository {

    // 멀티DB 의존성 주입
    /*
    @Autowired
    @Qualifier("originalDsl")
    DSLContext first;

    @Autowired
    @Qualifier("firstDsl")
    DSLContext first;

    @Autowired
    @Qualifier("secondDsl")
    DSLContext second;
    */
}
