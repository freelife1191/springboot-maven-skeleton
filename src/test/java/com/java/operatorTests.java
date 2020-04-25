package com.java;

import com.project.utils.common.ParseUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Created by KMS on 07/04/2020.
 */
public class operatorTests {

    @Test
    @DisplayName("비트 연산테스트")
    public void bitOperatorTest() {
        System.out.println(getParseValue(63));

    }

    /**
     * 2진 코드 파싱
     * @param value
     * @return
     */
    private int[] getParseValue(int value) {

        // 비트연산을 하여 확인한다.
        String result = Integer.toBinaryString(value);

        System.out.println("getParseValue ["+ result +"]");

        int[] values = new int[result.length()];
        if( value > 0 ) {
            char[] temp = result.toCharArray();

            for (int i = 0; i < temp.length; i++) {

                int pow_value = (int) Math.pow(2, i);
                System.out.println();
                System.out.println(i+", "+temp.length+", "+temp[temp.length - i - 1]);
                int param_value = ParseUtils.parseInt(String.valueOf(temp[temp.length - i - 1]));
                int return_value = pow_value * param_value;

                values[i] = return_value;
            }

            System.out.println("getParseValue values[" + Arrays.toString(values) + "]");
        }

        return values;
    }

}
