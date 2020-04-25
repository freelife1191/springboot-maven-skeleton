package com.java;

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
}
