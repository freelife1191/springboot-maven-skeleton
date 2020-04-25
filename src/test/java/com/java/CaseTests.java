package com.java;

import com.project.utils.common.CommonUtils;
import org.jooq.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.project.h2.entity.tables.JCommonDetailCode.COMMON_DETAIL_CODE;

/**
 * Created by KMS on 08/04/2020.
 */
public class CaseTests {

    @Test
    @DisplayName("스네이크 케이스 변환 테스트")
    public void SnakeCaseTest() {

    }

    @Test
    @DisplayName("카멜케이스 변환 테스트")
    public void CamelCaseTest() {
        for(Field field : COMMON_DETAIL_CODE.fields()) {
            System.out.println("/** "+field.getComment() +" **/");
            System.out.println("private "+field.getDataType().getType().getSimpleName()+" "+ CommonUtils.toCamelCase(field.getName()));
        }
    }
}
