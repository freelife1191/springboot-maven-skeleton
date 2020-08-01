package com.project.component.code.repository;

import com.google.common.collect.Lists;
import com.project.component.code.domain.CommonEmptyCode;
import com.project.utils.common.JooqUtils;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.project.h2.entity.tables.JCommonEmptyCode.COMMON_EMPTY_CODE;

/**
 * Created by KMS on 30/03/2020.
 */
@Repository
@RequiredArgsConstructor
@Transactional
public class CommonEmptyCodeRepository {

    private final DSLContext dsl;

    /**
     * 공통 누락 코드 등록
     * @param emptyCode
     * @return
     */
    public int insertCommonEmptyCode(CommonEmptyCode emptyCode) {

        List<Field<?>> fieldList = Lists.newArrayList(
                COMMON_EMPTY_CODE.GROUP_CODE,
                COMMON_EMPTY_CODE.DETAIL_CODE,
                COMMON_EMPTY_CODE.VALUE
        );

        InsertSetStep<?> insert = dsl.insertInto(COMMON_EMPTY_CODE);
        InsertSetMoreStep<?> moreStep = JooqUtils.setInsertSetMoreStep(insert, fieldList, emptyCode);
        moreStep.onDuplicateKeyIgnore();

        return moreStep.execute();

    }
}
