package com.java;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by KMS on 23/04/2020.
 */
public class ListRemoveTest {

    @Test
    public void defaultListRemove(){

        List<String> strList = new ArrayList<>(Arrays.asList(new String[] {"A","B","C","D","E"}));
        System.out.println(strList);

        System.out.println(strList.removeIf(data -> data.equals("A")));

    }
}
