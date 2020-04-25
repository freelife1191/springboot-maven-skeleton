package com.java;

import com.project.h2.entity.tables.JCommonCode;
import com.project.utils.common.JooqUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.project.h2.entity.tables.JCommonCode.COMMON_CODE;


/**
 * Created by KMS on 09/04/2020.
 */
public class ObjectCreator {

    @Test
    @DisplayName("Jooq 테이블 필드를 객체로 변환")
    public void objectCreator() {
        JooqUtils.makeObject(COMMON_CODE).forEach(System.out::println);
    }

    @Test
    @DisplayName("Jooq 테이블 필드를 객체로 변환 ApiModel 타입")
    public void objectCreatorApiModel() {
        JooqUtils.makeObject(COMMON_CODE, true).forEach(System.out::println);
    }

}