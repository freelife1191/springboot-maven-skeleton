package com.java;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * Created by KMS on 15/02/2020.
 */
public class StringTests {

    @Test
    void subStringTests() {
        String str = "선생님가방에들어가신다";
        System.out.println(str.substring(0,5));
    }

    @Test
    void stringByteCheck() {
        String str = "선생님가방에들어가신다";
        System.out.println(str.getBytes().length);
    }

    @Test
    void ifTests() {
        System.out.println(true && true);
    }

    @Test
    void stringLenghtTest() {
        String test = "안녕하세요";
        test = null;
        System.out.println(StringUtils.length(test));
        System.out.println(StringUtils.substring(test,0, 5));
    }
}
