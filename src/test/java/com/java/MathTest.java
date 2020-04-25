package com.java;

import org.junit.jupiter.api.Test;

/**
 * Created by KMS on 19/04/2020.
 */
public class MathTest {

    @Test
    public void roundTest() {
        float num = 3.145676f;

        int num1 = Math.round(num);

        System.out.println("ROUND = "+Math.round(num));
        System.out.println("CEIL = "+Math.ceil(num));
        System.out.println("FLOOR = "+Math.floor(num));
    }
}
