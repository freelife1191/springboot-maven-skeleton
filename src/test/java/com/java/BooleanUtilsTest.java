package com.java;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;

/**
 * Created by KMS on 18/04/2020.
 */
public class BooleanUtilsTest {

    @Test
    public void toBooleanUtilTest() {

        System.out.println(BooleanUtils.toBoolean("Y"));
        System.out.println(BooleanUtils.toBoolean("N"));
        System.out.println(BooleanUtils.toBoolean("y"));
        System.out.println(BooleanUtils.toBoolean("n"));
        System.out.println(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }
}
