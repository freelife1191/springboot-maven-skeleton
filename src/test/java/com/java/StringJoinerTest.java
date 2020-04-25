package com.java;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Created by KMS on 24/04/2020.
 */
public class StringJoinerTest {

    @Test
    public void joinTest() {
        String discount = "운영방법|주차장형태|SUV이용가능|파킹패스|결제가능수단|제휴사할인|간편결제|감면적용|장애인주차|여성전용주차|전기충전소";
        discount = null;
        System.out.println(Arrays.toString(discount.split("\\|")));
    }
}
