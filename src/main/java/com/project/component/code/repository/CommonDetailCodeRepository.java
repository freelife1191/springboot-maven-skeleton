package com.project.component.code.repository;

import com.google.common.collect.Lists;
import com.project.component.code.domain.CommonDetailCode;
import com.project.component.code.dto.CommonCodeDto;
import com.project.component.code.packet.ReqCommonDetailCodeGET;
import com.project.utils.common.CommonUtils;
import com.project.utils.common.JooqUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.project.h2.entity.tables.JCommonCode.COMMON_CODE;
import static com.project.h2.entity.tables.JCommonDetailCode.COMMON_DETAIL_CODE;
import static org.jooq.impl.DSL.*;

/**
 * 공통 상세 코드 Repository
 * Created by KMS on 26/03/2020.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class CommonDetailCodeRepository {

    private final DSLContext dsl;

    /**
     * 공통 상세 코드 단건 조회
     * @param id
     * @param code
     * @return
     */
    public CommonDetailCode findByCode(Integer id, Integer code) {
        if(id == null || code ==null) return null;

        List<Field<?>> selectFieldList = getDetailCodeFieldList();
        selectFieldList.add(COMMON_DETAIL_CODE.CREATED_ID);
        selectFieldList.add(COMMON_DETAIL_CODE.CREATED_AT);
        selectFieldList.add(COMMON_DETAIL_CODE.UPDATED_ID);
        selectFieldList.add(COMMON_DETAIL_CODE.UPDATED_AT);

        return dsl.select(selectFieldList)
                .from(COMMON_DETAIL_CODE)
                .where(COMMON_DETAIL_CODE.COMMON_CODE_ID.eq(id))
                .and(COMMON_DETAIL_CODE.DETAIL_CODE.eq(code))
                .limit(1)
                .fetchOneInto(CommonDetailCode.class);
    }

    /**
     * 공통 상세 코드 리스트 조회
     * 공통 메인 코드에 해당하는 공통 상세 코드 리스트 조회
     * @param id
     * @param detailCodes
     * @return
     */
    public List<CommonDetailCode> findByCodes(Integer id, List<Integer> detailCodes) {

        Condition condition = DSL.trueCondition();
        if(!ListUtils.emptyIfNull(detailCodes).isEmpty())
            condition.and(COMMON_DETAIL_CODE.DETAIL_CODE.in(detailCodes));

        List<Field<?>> selectFieldList = getDetailCodeFieldList();
        selectFieldList.add(COMMON_DETAIL_CODE.CREATED_ID);
        selectFieldList.add(COMMON_DETAIL_CODE.CREATED_AT);
        selectFieldList.add(COMMON_DETAIL_CODE.UPDATED_ID);
        selectFieldList.add(COMMON_DETAIL_CODE.UPDATED_AT);

        return dsl.select(selectFieldList)
                .from(COMMON_DETAIL_CODE)
                .where(COMMON_DETAIL_CODE.COMMON_CODE_ID.eq(id))
                .and(condition)
                .fetchInto(CommonDetailCode.class);
    }

    /**
     * 공통 상세 코드 조회 (페이징)
     * @param condition
     * @return
     */
    public Page<CommonDetailCode> selectCommonDetailCode(Pageable pageable, ReqCommonDetailCodeGET condition) {
        // 정렬 Field 셋팅
        List<Field<?>> sortFieldList = Lists.newArrayList(
                COMMON_DETAIL_CODE.COMMON_CODE_ID,
                COMMON_DETAIL_CODE.DETAIL_CODE,
                COMMON_DETAIL_CODE.DETAIL_CODE_NM,
                COMMON_DETAIL_CODE.DETAIL_CODE_ENG_NM,
                COMMON_DETAIL_CODE.DETAIL_CODE_DC,
                COMMON_DETAIL_CODE.ORDER,
                COMMON_DETAIL_CODE.ENABLED
        );

        // 정렬 Field 외에 항상 기본적으로 정렬하기 위한 Field 설정
        // 기본적으로 정렬하는 Field 의 경우 Pageable로 요청들어온 Field 보다 뒤에 위치함
        List<Field<?>> defaultSortFieldList = Lists.newArrayList(
                COMMON_DETAIL_CODE.COMMON_CODE_ID,
                COMMON_DETAIL_CODE.DETAIL_CODE,
                COMMON_DETAIL_CODE.DETAIL_CODE_NM,
                COMMON_DETAIL_CODE.ENABLED
        );

        // 가장 우선순위가 높은 정렬 Field
        List<SortField<?>> preSortField = JooqUtils.getDefaultSortFieldList(COMMON_DETAIL_CODE.ORDER);

        // 조회 필드 셋팅
        List<Field<?>> selectFieldList = JooqUtils.getFieldList(getDetailCodeFieldList(),
                COMMON_DETAIL_CODE.CREATED_ID,
                COMMON_DETAIL_CODE.CREATED_AT,
                COMMON_DETAIL_CODE.UPDATED_ID,
                COMMON_DETAIL_CODE.UPDATED_AT);

        // 데이터 조회
        List<CommonDetailCode> commonDetailCodes = setCommonDetailCodeConditionStep(dsl.select(selectFieldList), condition)
                .orderBy(JooqUtils.getDefaultSortFieldList(defaultSortFieldList, JooqUtils.getSortFields(preSortField, pageable.getSort(), sortFieldList))) // Pageable Sort 로 정렬
                .limit(pageable.getPageSize()).offset(pageable.getOffset()) // Pageable 객체로 페이징 처리
                .fetchInto(CommonDetailCode.class);

        // 총 데이터수 조회
        long totalCount = setCommonDetailCodeConditionStep(dsl.select(count()), condition).fetchOneInto(int.class);

        return new PageImpl<>(commonDetailCodes, pageable, totalCount);
    }

    /**
     * 공통 상세 코드 조회 조건
     * @param select
     * @param condition
     * @param <T>
     * @return
     */
    private <T extends Record> SelectConditionStep<T> setCommonDetailCodeConditionStep(SelectSelectStep<T> select, ReqCommonDetailCodeGET condition) {

        List<Condition> conditionList = JooqUtils.setConditionList(getDetailCodeFieldList(), condition);

        return select.from(COMMON_DETAIL_CODE)
                .where(and(conditionList));
    }

    /**
     * 공통 메인 코드 공통 상세 코드 조인 데이터 조회
     * @param condition
     * @return
     */
    public List<CommonCodeDto> selectCommonCodeJoin(CommonCodeDto condition) {
        List<Condition> codeConditionList = JooqUtils.setConditionList(getCodeFieldList(), condition.getCode());
        List<Condition> detailCodeConditionList = JooqUtils.setConditionList(getDetailCodeFieldList(), condition.getDetailCode());

        return dsl.select(getSelectFieldList())
                .from(COMMON_CODE.leftJoin(COMMON_DETAIL_CODE)
                        .on(COMMON_CODE.ID.eq(COMMON_DETAIL_CODE.COMMON_CODE_ID))
                        .and(DSL.and(detailCodeConditionList)))
                .where(DSL.and(codeConditionList))
                .orderBy(COMMON_DETAIL_CODE.COMMON_CODE_ID.asc(), COMMON_DETAIL_CODE.DETAIL_CODE.asc())
                .fetchInto(CommonCodeDto.class);
    }

    /**
     * 공통 상세 코드 등록
     * @param commonDetailCode
     * @return
     */
    public int insertCommonDetailCode(CommonDetailCode commonDetailCode) {

        if (CommonUtils.objectEmpty(commonDetailCode)) return 0;

        if (Objects.isNull(commonDetailCode.getCommonCodeId())) return 0;

        // 공통메인코드 하위의 코드이므로 이전 코드에 +1하는 형태로 등록(DETAIL_CODE는 제외처리)
        List<Field<?>> outFieldList = Lists.newArrayList(COMMON_DETAIL_CODE.DETAIL_CODE);
        List<Field<?>> insertFieldList = JooqUtils.getFieldList(getDetailCodeFieldList(), outFieldList);

        InsertSetStep<?> insert = dsl.insertInto(COMMON_DETAIL_CODE);

        InsertSetMoreStep<?> moreStep = insert
                .set(COMMON_DETAIL_CODE.DETAIL_CODE, getMaxCommonDetailCode(commonDetailCode.getCommonCodeId()));

        moreStep = JooqUtils.setInsertSetMoreStep(moreStep, insertFieldList, commonDetailCode);

        return moreStep.returning(COMMON_DETAIL_CODE.DETAIL_CODE).fetchOne().get(COMMON_DETAIL_CODE.DETAIL_CODE);

    }

    /**
     * 공통 상세 코드 다중 등록
     * @param commonDetailCodeList
     * @return
     */
    public List<Integer> insertCommonDetailCode(Integer id, List<CommonDetailCode> commonDetailCodeList) {
        // 다중 등록된 공통 상세 코드 리스트
        List<Integer> resultDetailCodeList = new ArrayList<>();

        if (ListUtils.emptyIfNull(commonDetailCodeList).isEmpty()) return resultDetailCodeList;

        Integer detailCode = getMaxCommonDetailCode(id);

        List<InsertSetMoreStep<?>> valueList = new ArrayList<>();
        for(CommonDetailCode detailCodeDto : commonDetailCodeList) {

            resultDetailCodeList.add(detailCode);
            detailCodeDto.setDetailCode(detailCode);

            InsertSetStep<?> insert = dsl.insertInto(COMMON_DETAIL_CODE);
            InsertSetMoreStep<?> moreStep = JooqUtils.setInsertSetMoreStep(insert, getDetailCodeFieldList(), detailCodeDto, false, false);
            valueList.add(moreStep);
            detailCode++;
        }
        dsl.batch(valueList).execute();

        return resultDetailCodeList;
    }

    /**
     * 공통 상세 코드 조회 + 1
     * @param commonCodeId
     * @return
     */
    public int getMaxCommonDetailCode(Integer commonCodeId) {
        return dsl.select(ifnull(max(COMMON_DETAIL_CODE.DETAIL_CODE), 0).plus(1))
                .from(COMMON_DETAIL_CODE)
                .where(COMMON_DETAIL_CODE.COMMON_CODE_ID.eq(commonCodeId))
                .fetchOne().value1();
    }

    /**
     * 공통 상세 코드 업데이트
     * @param commonDetailCode
     * @return
     */
    public int updateCommonDetailCode(CommonDetailCode commonDetailCode) {
        return updateCommonDetailCode(commonDetailCode, null);
    }

    /**
     * 공통 상세 코드 업데이트
     * @param commonDetailCode
     * @param condition 업데이트 조건
     * @return
     */
    public int updateCommonDetailCode(CommonDetailCode commonDetailCode, CommonDetailCode condition) {
        if (CommonUtils.objectEmpty(commonDetailCode)) return 0;

        List<Field<?>> outFieldList = Lists.newArrayList(COMMON_DETAIL_CODE.COMMON_CODE_ID, COMMON_DETAIL_CODE.DETAIL_CODE);
        List<Field<?>> updateFieldList = JooqUtils.getFieldList(getDetailCodeFieldList(), outFieldList);

        UpdateSetFirstStep<?> update = dsl.update(COMMON_DETAIL_CODE);
        UpdateSetMoreStep<?> moreStep = update
                .set(COMMON_DETAIL_CODE.UPDATED_AT, LocalDateTime.now());

        moreStep = JooqUtils.setUpdateSetMoreStep(moreStep, updateFieldList, commonDetailCode);

        return moreStep
                .where(COMMON_DETAIL_CODE.COMMON_CODE_ID.eq(commonDetailCode.getCommonCodeId()))
                .and(COMMON_DETAIL_CODE.DETAIL_CODE.eq(commonDetailCode.getDetailCode()))
                .and(DSL.and(JooqUtils.setConditionList(updateFieldList, condition)))
                .execute();
    }

    /**
     * 공통 상세 코드 삭제
     * @param commonCodeId
     * @param detailCode
     * @return
     */
    public int deleteCommonDetailCode(Integer commonCodeId, Integer detailCode) {
        return dsl.delete(COMMON_DETAIL_CODE)
                .where(COMMON_DETAIL_CODE.COMMON_CODE_ID.eq(commonCodeId))
                .and(COMMON_DETAIL_CODE.DETAIL_CODE.eq(detailCode))
                .execute();
    }

    /**
     * 공통 코드 JOIN Select Field 리스트
     * @return
     */
    private List<Field<?>> getSelectFieldList() {
        return Lists.newArrayList(
                COMMON_CODE.ID.as("code.id"),
                COMMON_CODE.GROUP_CODE.as("code.groupCode"),
                COMMON_CODE.CODE.as("code.code"),
                COMMON_CODE.CODE_NM.as("code.codeNm"),
                COMMON_CODE.CODE_ENG_NM.as("code.codeEngNm"),
                COMMON_CODE.CODE_DC.as("code.codeDc"),
                COMMON_CODE.UPPER_ID.as("code.upperId"),
                COMMON_CODE.DEPTH.as("code.depth"),
                COMMON_CODE.ORDER.as("code.order"),
                COMMON_CODE.PATH.as("code.path"),
                COMMON_CODE.PATH_NM.as("code.pathNm"),
                COMMON_CODE.ENABLED.as("code.enabled"),
                COMMON_DETAIL_CODE.COMMON_CODE_ID.as("detailCode.commonCodeId"),
                COMMON_DETAIL_CODE.DETAIL_CODE.as("detailCode.detailCode"),
                COMMON_DETAIL_CODE.DETAIL_CODE_NM.as("detailCode.detailCodeNm"),
                COMMON_DETAIL_CODE.DETAIL_CODE_ENG_NM.as("detailCode.detailCodeEngNm"),
                COMMON_DETAIL_CODE.DETAIL_CODE_DC.as("detailCode.detailCodeDc"),
                COMMON_DETAIL_CODE.ORDER.as("detailCode.order"),
                COMMON_DETAIL_CODE.ENABLED.as("detailCode.enabled"),
                COMMON_DETAIL_CODE.ETC1.as("detailCode.etc1"),
                COMMON_DETAIL_CODE.ETC2.as("detailCode.etc2"),
                COMMON_DETAIL_CODE.ETC3.as("detailCode.etc3"),
                COMMON_DETAIL_CODE.ETC4.as("detailCode.etc4"),
                COMMON_DETAIL_CODE.ETC5.as("detailCode.etc5")
        );
    }

    /**
     * 공통 메인 코드 Field 리스트
     * @return
     */
    private List<Field<?>> getCodeFieldList() {
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

    /**
     * 공통 상세 코드 Field 리스트
     * @return
     */
    private List<Field<?>> getDetailCodeFieldList() {
        return Lists.newArrayList(
                COMMON_DETAIL_CODE.COMMON_CODE_ID,
                COMMON_DETAIL_CODE.DETAIL_CODE,
                COMMON_DETAIL_CODE.DETAIL_CODE_NM,
                COMMON_DETAIL_CODE.DETAIL_CODE_ENG_NM,
                COMMON_DETAIL_CODE.DETAIL_CODE_DC,
                COMMON_DETAIL_CODE.ORDER,
                COMMON_DETAIL_CODE.ENABLED,
                COMMON_DETAIL_CODE.ETC1,
                COMMON_DETAIL_CODE.ETC2,
                COMMON_DETAIL_CODE.ETC3,
                COMMON_DETAIL_CODE.ETC4,
                COMMON_DETAIL_CODE.ETC5
        );
    }
}
