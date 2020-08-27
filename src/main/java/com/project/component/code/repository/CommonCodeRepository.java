package com.project.component.code.repository;

import com.google.common.collect.Lists;
import com.project.component.code.domain.CommonCode;
import com.project.component.code.packet.ReqCommonCodeGET;
import com.project.component.code.packet.ResCommonCode;
import com.project.utils.common.CommonUtils;
import com.project.utils.common.JooqUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.project.h2.entity.tables.JCommonCode.COMMON_CODE;
import static org.jooq.impl.DSL.*;

/**
 * Created by KMS on 25/03/2020.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class CommonCodeRepository {

    private final DSLContext dsl;

    /**
     * 공통 메인 코드 단건 조회
     * @param id
     * @return
     */
    public ResCommonCode findById(Integer id) {
        if(Objects.isNull(id)) return null;

        List<Field<?>> selectFieldList = getFieldList();
        selectFieldList.add(COMMON_CODE.CREATED_ID);
        selectFieldList.add(COMMON_CODE.CREATED_AT);
        selectFieldList.add(COMMON_CODE.UPDATED_ID);
        selectFieldList.add(COMMON_CODE.UPDATED_AT);

        return dsl.select(selectFieldList)
                .from(COMMON_CODE)
                .where(COMMON_CODE.ID.eq(id))
                .limit(1)
                .fetchOneInto(ResCommonCode.class);
    }

    /**
     * 공통 메인 코드 리스트 조회 (페이징)
     * @param condition
     * @return
     */
    public Page<ResCommonCode> selectCommonCode(Pageable pageable, ReqCommonCodeGET condition) {
        // 정렬 Field 셋팅
        List<Field<?>> sortFieldList = Lists.newArrayList(
                COMMON_CODE.ID,
                COMMON_CODE.CODE,
                COMMON_CODE.CODE_NM,
                COMMON_CODE.CODE_ENG_NM,
                COMMON_CODE.CODE_DC,
                COMMON_CODE.ORDER,
                COMMON_CODE.ENABLED
        );

        // 정렬 Field 외에 항상 기본적으로 정렬하기 위한 Field 설정
        // 기본적으로 정렬하는 Field 의 경우 Pageable로 요청들어온 Field 보다 뒤에 위치함
        List<Field<?>> defaultSortFieldList = Lists.newArrayList(
                COMMON_CODE.ID,
                COMMON_CODE.CODE,
                COMMON_CODE.CODE_NM,
                COMMON_CODE.ENABLED
        );

        // 가장 우선순위가 높은 정렬 Field
        List<SortField<?>> preSortField = JooqUtils.getDefaultSortFieldList(COMMON_CODE.ORDER);

        // 조회 필드 셋팅
        List<Field<?>> selectFieldList = JooqUtils.getFieldList(getFieldList(),
                COMMON_CODE.CREATED_ID,
                COMMON_CODE.CREATED_AT,
                COMMON_CODE.UPDATED_ID,
                COMMON_CODE.UPDATED_AT);

        List<ResCommonCode> commonCodes = setCommonCodeConditionStep(dsl.select(selectFieldList), condition)
                .orderBy(JooqUtils.getDefaultSortFieldList(defaultSortFieldList, JooqUtils.getSortFields(preSortField, pageable.getSort(), sortFieldList))) // Pageable Sort 로 정렬
                .limit(pageable.getPageSize()).offset(pageable.getOffset()) // Pageable 객체로 페이징 처리
                .fetchInto(ResCommonCode.class);

        // 총 데이터수 조회
        long totalCount = setCommonCodeConditionStep(dsl.select(count()), condition).fetchOneInto(int.class);

        return new PageImpl<>(commonCodes, pageable, totalCount);
    }

    /**
     * 공통 코드 조회 조건
     * @param select
     * @param condition
     * @param <T>
     * @return
     */
    private <T extends Record> SelectConditionStep<T> setCommonCodeConditionStep(SelectSelectStep<T> select, ReqCommonCodeGET condition) {

        List<Condition> conditionList = JooqUtils.setConditionList(getFieldList(), condition);

        return select.from(COMMON_CODE)
                .where(and(conditionList));
    }

    /**
     * 공통 메인 코드 단건등록
     * @param commonCode
     * @return
     */
    public int insertCommonCode(CommonCode commonCode) {
        if(CommonUtils.objectEmpty(commonCode)) return 0;

        List<Field<?>> selectFieldList = getFieldList();
        selectFieldList.add(COMMON_CODE.CREATED_ID);
        selectFieldList.add(COMMON_CODE.CREATED_AT);
        selectFieldList.add(COMMON_CODE.UPDATED_ID);
        selectFieldList.add(COMMON_CODE.UPDATED_AT);

        List<Field<?>> fieldList = JooqUtils.getFieldList(getFieldList(), Lists.newArrayList(COMMON_CODE.CODE));

        InsertSetStep<?> insert = dsl.insertInto(COMMON_CODE);

        InsertSetMoreStep<?> moreStep = insert
                .set(COMMON_CODE.CODE, getMaxCommonCode(commonCode.getGroupCode()));

        moreStep = JooqUtils.setInsertSetMoreStep(moreStep, fieldList, commonCode, false, false);
        return moreStep.returning(COMMON_CODE.ID).fetchOne().get(COMMON_CODE.ID);
    }

    /**
     * 공통 메인 코드 조회 + 1
     * 공통메인코드 그룹코드 하위의 단순증가 숫자값을 코드로 등록함
     * @param groupCode
     * @return
     */
    public int getMaxCommonCode(String groupCode) {
        return dsl.select(ifnull(max(COMMON_CODE.CODE), 0).plus(1))
                .from(COMMON_CODE)
                .where(COMMON_CODE.GROUP_CODE.eq(groupCode))
                .fetchOne().value1();
    }

    /**
     * 공통 메인 코드 업데이트
     * @param commonCode
     * @return
     */
    public int updateCommonCode(CommonCode commonCode) {
        return updateCommonCode(commonCode, null);
    }

    /**
     * 공통 메인 코드 업데이트
     * @param commonCode
     * @param condition
     * @return
     */
    public int updateCommonCode(CommonCode commonCode, CommonCode condition) {
        if(CommonUtils.objectEmpty(commonCode)) return 0;

        List<Field<?>> outFieldList = Lists.newArrayList(COMMON_CODE.ID);
        List<Field<?>> updateFieldList = JooqUtils.getFieldList(getFieldList(), outFieldList);

        UpdateSetFirstStep<?> update = dsl.update(COMMON_CODE);
        UpdateSetMoreStep<?> moreStep = update
                .set(COMMON_CODE.UPDATED_AT, LocalDateTime.now());

        moreStep = JooqUtils.setUpdateSetMoreStep(moreStep, updateFieldList, commonCode);

        return moreStep
                .where(COMMON_CODE.ID.eq(commonCode.getId()))
                .and(DSL.and(JooqUtils.setConditionList(updateFieldList, condition)))
                .execute();
    }

    /**
     * 공통 메인 코드 삭제
     * @param id
     * @return
     */
    public int deleteCommonCode(Integer id) {
        return dsl.delete(COMMON_CODE)
                .where(COMMON_CODE.ID.eq(id))
                .execute();
    }

    /**
     * 공통 코드 Field 리스트
     * @return
     */
    private List<Field<?>> getFieldList() {
        return Lists.newArrayList(
                COMMON_CODE.ID,
                COMMON_CODE.GROUP_CODE,
                COMMON_CODE.CODE,
                COMMON_CODE.CODE_NM,
                COMMON_CODE.CODE_ENG_NM,
                COMMON_CODE.CODE_DC,
                COMMON_CODE.UPPER_ID,
                COMMON_CODE.DEPTH,
                COMMON_CODE.ORDER,
                COMMON_CODE.PATH,
                COMMON_CODE.PATH_NM,
                COMMON_CODE.ENABLED
        );
    }

}
