package com.java;

import com.google.common.collect.Lists;
import com.project.component.code.domain.CommonCode;
import com.project.utils.common.CommonUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Created by KMS on 09/02/2020.
 */
public class ObjectTests {

    @Test
    void ObjectArrayTest() {

        List<Object> objectList = Lists.newArrayList(
                null,
                null,
                "a",
                null
                ,1
        );

        System.out.println(objectList.toArray()[0]);
        /*
        for (Object obj : objectList.toArray()) {
            System.out.println(obj);
        }
        */
        // System.out.println(objectList.toArray());

    }

    @Test
    void ObjectEmptyIsZeroTest() {
        CommonCode data = CommonCode.builder().code(0).codeDc("").build();
        System.out.println("## ObjectEmpty = "+ CommonUtils.objectEmpty(data, true));
    }

    @Test
    void ObjectIsProcessTest() {
        String data = "";
        boolean emptyOption = false;
        System.out.println("## isProcess = "+ CommonUtils.isProcess(false, emptyOption, data));
        System.out.println("## strEmptyIfNull = "+ CommonUtils.strEmptyIfNull(emptyOption, data));
    }

}
