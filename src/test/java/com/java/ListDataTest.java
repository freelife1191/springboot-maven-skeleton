package com.java;

import com.project.component.code.domain.CommonDetailCode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KMS on 20/04/2020.
 */
public class ListDataTest {

    @Test
    public void listData() {
        List<CommonDetailCode> commonDetailCodeList = new ArrayList<>();
        commonDetailCodeList.add(CommonDetailCode.builder().commonCodeId(1).build());
        commonDetailCodeList.add(CommonDetailCode.builder().commonCodeId(2).build());
        commonDetailCodeList.add(CommonDetailCode.builder().commonCodeId(3).build());

        int i = 0;
        for (CommonDetailCode detailCode: commonDetailCodeList) {
            detailCode.setDetailCode(i++);
        }

        commonDetailCodeList.forEach(data -> System.out.println(data));
    }
}
