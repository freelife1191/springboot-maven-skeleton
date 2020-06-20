package com.project.utils.common;

import com.google.common.collect.Lists;
import com.project.utils.common.constant.ConditionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.project.utils.common.constant.ConditionType.*;
import static org.jooq.impl.DSL.value;
import static org.jooq.meta.mysql.information_schema.InformationSchema.INFORMATION_SCHEMA;

/**
 * Created by KMS on 26/11/2019.
 * Jooq Utils
 */
@Slf4j
public class JooqUtils {

    /**
     * TableField 값과 Object 를 받아 ConditionList를 셋팅함
     * 객체와 조건을 추가할 TableFieldList를 함께 넘겨 null이 아닌 값은 자동으로 조건에 추가하는 함수
     *
     * Select Where 조건에 해당되는 TableField 리스트를 생성
     * List<TableField> fieldList = new ArrayList<>();
     * fieldList.add(SERVICE_DETAIL_CODE.SERVICE_CODE);
     *
     * conditionList 생성
     * List<Condition> conditionsList = setConditionList(fieldList, object);
     *
     * 생성된 conditionList를 WHERE 조건에 적용
     * .where(DSL.and(conditionsList))
     *
     * @param fieldList List<Field> 테이블 필드 리스트
     * @param object 맵핑할 객체
     * @return List<Condition> 셋팅된 컨디션 리스트
     */
    public static List<Condition> setConditionList(List<Field> fieldList, Object object) {
        return setConditionList(null, fieldList, object, eq);
    }

    /**
     * nullOption null 값 허용(true), 미허용(false) 기본값 미허용(false)
     * emptyOption String Empty 값 허용(true), 미허용(false) 기본값 미허용(false)
     * @param fieldList
     * @param object
     * @param emptyOption
     * @param nullOption
     * @return
     */
    public static List<Condition> setConditionList(List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption) {
        return setConditionList(null, fieldList, object, eq, nullOption, emptyOption);
    }

    public static List<Condition> setConditionList(List<Condition> conditionList, List<Field> fieldList, Object object) {
        return setConditionList(conditionList, fieldList, object, eq);
    }

    /**
     * nullOption null 값 허용(true), 미허용(false) 기본값 미허용(false)
     * emptyOption String Empty 값 허용(true), 미허용(false) 기본값 미허용(false)
     * @param conditionList
     * @param fieldList
     * @param object
     * @param emptyOption
     * @param nullOption
     * @return
     */
    public static List<Condition> setConditionList(List<Condition> conditionList, List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption) {
        return setConditionList(conditionList, fieldList, object, eq, nullOption, emptyOption);
    }

    /**
     * 기존 conditionList와 TableField 값과 Object 를 받아 ConditionList를 셋팅함
     * 객체와 조건을 추가할 TableFieldList를 함께 넘겨 null이 아닌 값은 자동으로 조건에 추가하는 함수
     *
     * Select Where 조건에 해당되는 TableField 리스트를 생성
     * List<TableField> fieldList = new ArrayList<>();
     * fieldList.add(SERVICE_DETAIL_CODE.SERVICE_CODE);`
     *
     * 기존 conditionList에 추가해서 conditionList 생성
     * List<Condition> conditionsList = setConditionList(conditionList, fieldList, object, conditionType);
     *
     * 생성된 conditionList를 WHERE 조건에 적용
     * .where(DSL.and(conditionsList))
     *
     * @param conditionList
     * @param fieldList
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    public static List<Condition> setConditionList(List<Condition> conditionList, List<Field> fieldList, Object object, ConditionType conditionType) {
        return setConditionList(conditionList, fieldList, object, conditionType, false, false);
    }

    /**
     * 기존 conditionList와 TableField 값과 Object 를 받아 ConditionList를 셋팅함
     * 객체와 조건을 추가할 TableFieldList를 함께 넘겨 null이 아닌 값은 자동으로 조건에 추가하는 함수
     *
     * Select Where 조건에 해당되는 TableField 리스트를 생성
     * List<TableField> fieldList = new ArrayList<>();
     * fieldList.add(SERVICE_DETAIL_CODE.SERVICE_CODE);`
     *
     * List<Condition> conditionsList = setConditionList(conditionList, fieldList, object, conditionType, nullOption, emptyOption);
     *
     * 생성된 conditionList를 WHERE 조건에 적용
     * .where(DSL.and(conditionsList))
     *
     * @param conditionList
     * @param fieldList
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    public static List<Condition> setConditionList(List<Condition> conditionList, List<Field> fieldList, Object object, ConditionType conditionType, boolean nullOption, boolean emptyOption) {

        if(conditionList == null) conditionList = new ArrayList<>();

        if(fieldList != null && Objects.nonNull(object)) {
            for (Field field : fieldList) {
                Map<String, Object> map = CommonUtils.objectToMap(object);
                String fieldCamelCaseName = CaseUtils.toCamelCase(field.getName(), false, new char[]{'_'});

                Object conditionData = CommonUtils.strEmptyIfNull(emptyOption, map.get(fieldCamelCaseName));
                //프로세스 진행여부 판단 후 진행
                if (CommonUtils.isProcess(nullOption, conditionData)) {
                    switch (conditionType) {
                        case like:
                            conditionList.add(field.like("%"+conditionData+"%"));
                            break;
                        case notLike:
                            conditionList.add(field.notLike("%"+conditionData+"%"));
                            break;
                        case contains:
                            conditionList.add(field.contains(conditionData));
                            break;
                        case startsWith:
                            conditionList.add(field.startsWith(conditionData));
                            break;
                        case endsWith:
                            conditionList.add(field.endsWith(conditionData));
                            break;
                        case in:
                            conditionList.add(field.in(conditionData));
                            break;
                        case notIn:
                            conditionList.add(field.notIn(conditionData));
                            break;
                        case ne:
                            conditionList.add(field.ne(conditionData));
                            break;
                        case lt:
                            conditionList.add(field.lt(conditionData));
                            break;
                        case le:
                            conditionList.add(field.le(conditionData));
                            break;
                        case ge:
                            conditionList.add(field.ge(conditionData));
                            break;
                        case lessThan:
                            conditionList.add(field.lessThan(conditionData));
                            break;
                        case lessOrEqual:
                            conditionList.add(field.lessOrEqual(conditionData));
                            break;
                        case greaterThan:
                            conditionList.add(field.greaterThan(conditionData));
                            break;
                        case greaterOrEqual:
                            conditionList.add(field.greaterOrEqual(conditionData));
                            break;
                        case isTrue:
                            conditionList.add(field.isTrue());
                            break;
                        case isFalse:
                            conditionList.add(field.isFalse());
                            break;
                        case isNull:
                            conditionList.add(field.isNull());
                            break;
                        case isNotNull:
                            conditionList.add(field.isNotNull());
                            break;
                        case notEqual:
                            conditionList.add(field.notEqual(conditionData));
                            break;
                        default:
                            conditionList.add(field.eq(conditionData));
                    }

                }
            }
        }
        return conditionList;
    }

    /**
     * Object의 null이 아닌 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 InsertSetMoreStep에 에 추가 적용
     * @param insert
     * @param fieldList
     * @param object
     * @return
     */
    public static InsertSetMoreStep setInsertSetMoreStep(InsertSetStep insert, List<Field> fieldList, Object object){
        InsertSetMoreStep moreStep = (InsertSetMoreStep) insert;
        return setInsertSetMoreStep(moreStep, fieldList, object, false, false, true);
    }

    /**
     * Object의 null이 아닌 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 InsertSetMoreStep에 에 추가 적용
     * @param insert
     * @param fieldList
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: empty 허용, false: empty 미허용
     * @return
     */
    public static InsertSetMoreStep setInsertSetMoreStep(InsertSetStep insert, List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption){
        InsertSetMoreStep moreStep = (InsertSetMoreStep) insert;
        return setInsertSetMoreStep(moreStep, fieldList, object, nullOption, emptyOption, true);
    }

    /**
     * Object의 null이 아닌 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 InsertSetMoreStep에 에 추가 적용
     * @param insert
     * @param fieldList
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: empty 허용, false: empty 미허용
     * @param isIpOption true: IP 옵션 적용, false: IP 옵션 미적용
     * @return
     */
    public static InsertSetMoreStep setInsertSetMoreStep(InsertSetStep insert, List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption, boolean isIpOption){
        InsertSetMoreStep moreStep = (InsertSetMoreStep) insert;
        return setInsertSetMoreStep(moreStep, fieldList, object, nullOption, emptyOption, isIpOption);
    }

    /**
     * Object의 null이 아닌 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 InsertSetMoreStep에 에 추가 적용
     * @param moreStep
     * @param fieldList
     * @param object
     * @return
     */
    public static InsertSetMoreStep setInsertSetMoreStep(InsertSetMoreStep moreStep, List<Field> fieldList, Object object){
        return setInsertSetMoreStep(moreStep, fieldList, object, false, false, true);
    }

    /**
     * Object의 null이 아닌 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 InsertSetMoreStep에 에 추가 적용
     * @param moreStep
     * @param fieldList
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: empty 허용, false: empty 미허용
     * @return
     */
    public static InsertSetMoreStep setInsertSetMoreStep(InsertSetMoreStep moreStep, List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption){
        return setInsertSetMoreStep(moreStep, fieldList, object, nullOption, emptyOption, true);
    }

    /**
     * Object의 null이 아닌 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 InsertSetMoreStep에 에 추가 적용
     * @param moreStep
     * @param fieldList
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: empty 허용, false: empty 미허용
     * @param isIpOption true: IP 옵션 적용, false: IP 옵션 미적용
     * @return
     */
    public static InsertSetMoreStep setInsertSetMoreStep(InsertSetMoreStep moreStep, List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption, boolean isIpOption ){

        if(moreStep == null) return null;

        if(fieldList != null && Objects.nonNull(object)) {
            for (Field field : fieldList) {
                Object objectValue = CommonUtils.strEmptyIfNull(emptyOption, getObjectValue(field, object));

                if (CommonUtils.isProcess(nullOption, objectValue)) {
                    //moreStep.set(field, CommonUtils.strEmptyIfNull(emptyOption, objectValue));
                    if(field.getName().toLowerCase().endsWith("ip") && isIpOption )
                        objectValue = setConvertIp(String.valueOf(objectValue));
                    moreStep.set(field, objectValue);
                    /*
                    if (objectValue instanceof String) {
                        if (StringUtils.isNotEmpty((String) objectValue))
                            moreStep.set(field, objectValue);
                    } else moreStep.set(field, objectValue);
                    */
                }
            }
        }
        return moreStep;
    }


    /**
     * Object의 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 UpdateSetMoreStep 에 추가 적용
     * nullOption false: null 미허용 기본값
     * @param update
     * @param fieldList
     * @param object
     * @return
     */
    public static UpdateSetMoreStep setUpdateSetMoreStep(UpdateSetStep update, List<Field> fieldList, Object object){
        UpdateSetMoreStep moreStep = (UpdateSetMoreStep) update;
        return setUpdateSetMoreStep(moreStep, fieldList, object, false, false);
    }

    /**
     * Object의 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 UpdateSetMoreStep 에 추가 적용
     * @param update
     * @param fieldList
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @return
     */
    public static UpdateSetMoreStep setUpdateSetMoreStep(UpdateSetStep update, List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption){
        return setUpdateSetMoreStep(update, fieldList, object, nullOption, emptyOption, true);
    }

    /**
     * Object의 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 UpdateSetMoreStep 에 추가 적용
     * @param update
     * @param fieldList
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: empty 허용, false: empty 미허용
     * @param isIpOption true: IP 옵션 적용, false: IP 옵션 미적용
     * @return
     */
    public static UpdateSetMoreStep setUpdateSetMoreStep(UpdateSetStep update, List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption, boolean isIpOption){
        UpdateSetMoreStep moreStep = (UpdateSetMoreStep) update;
        return setUpdateSetMoreStep(moreStep, fieldList, object, nullOption, emptyOption, isIpOption);
    }

    /**
     * Object의 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 UpdateSetMoreStep 에 추가 적용
     * @param moreStep
     * @param fieldList
     * @param object
     * @return
     */
    public static UpdateSetMoreStep setUpdateSetMoreStep(UpdateSetMoreStep moreStep, List<Field> fieldList, Object object){
        return setUpdateSetMoreStep(moreStep, fieldList, object, false, false);
    }

    /**
     * Object의 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 UpdateSetMoreStep 에 추가 적용
     * @param moreStep
     * @param fieldList
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: empty 허용, false: empty 미허용
     * @return
     */
    public static UpdateSetMoreStep setUpdateSetMoreStep(UpdateSetMoreStep moreStep, List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption ){
        return setUpdateSetMoreStep(moreStep, fieldList, object, nullOption, emptyOption, true);
    }

    /**
     * Object의 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 UpdateSetMoreStep 에 추가 적용
     * @param moreStep
     * @param fieldList
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: empty 허용, false: empty 미허용
     * @param isIpOption true: IP 옵션 적용, false: IP 옵션 미적용
     * @return
     */
    public static UpdateSetMoreStep setUpdateSetMoreStep(UpdateSetMoreStep moreStep, List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption, boolean isIpOption ){

        if(moreStep == null) return null;

        if(fieldList != null && Objects.nonNull(object)) {
            for (Field field : fieldList) {
                Object objectValue = CommonUtils.strEmptyIfNull(emptyOption, getObjectValue(field, object));

                if (CommonUtils.isProcess(nullOption, objectValue)) {
                    // moreStep.set(field, CommonUtils.strEmptyIfNull(emptyOption, objectValue));
                    if(field.getName().toLowerCase().endsWith("ip") && isIpOption )
                        objectValue = setConvertIp(String.valueOf(objectValue));
                    moreStep.set(field, objectValue);
                    /*
                    if (objectValue instanceof String) {
                        if (StringUtils.isNotEmpty((String) objectValue))
                            moreStep.set(field, objectValue);
                    } else moreStep.set(field, objectValue);
                    */
                }
            }
        }
        return moreStep;
    }

    /**
     * Insert Values 필드리스트 기반 오브젝트 값 리스트 생성 유틸
     * 필드리스트에 해당되는 오브젝트의 값들을 리스트에 담아서 리턴해줌
     * 기본값으로 null 데이터를 허용 처리함
     * @param fieldList
     * @param object
     * @return
     */
    public static List<Object> getObjectValueList(List<Field> fieldList, Object object) {
        return getObjectValueList(fieldList, object, false, false);
    }

    /**
     * Insert Values 필드리스트 기반 오브젝트 값 리스트 생성 유틸
     * 필드리스트에 해당되는 오브젝트의 값들을 리스트에 담아서 리턴해줌
     * @param fieldList
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: null 허용, false: null 미허용
     * @return
     */
    public static List<Object> getObjectValueList(List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption) {
        if(object == null) return null;

        List<Object> resultList = new ArrayList<>();

        if(fieldList != null && Objects.nonNull(object)) {
            for (Field field : fieldList) {
                Object objectValue = CommonUtils.strEmptyIfNull(emptyOption, getObjectValue(field, object));

                if (CommonUtils.isProcess(nullOption, objectValue))
                    //emptyOption 이 true 이고 String 타입이면 공백문자 값 add 아니면 null 로 변환하여 add
                    resultList.add(objectValue);
            }
        }
        return resultList;
    }

    /**
     * 오브젝트 값 가져오기
     * @param field
     * @param object
     * @return
     */
    private static Object getObjectValue(Field field, Object object){

        if(field == null && Objects.isNull(object)) return null;

        Map<String, Object> map = CommonUtils.objectToMap(object);

        String fieldCamelCaseName = CaseUtils.toCamelCase(field.getName(), false, new char[]{'_'});
        if(field.getName().startsWith("_")) fieldCamelCaseName = "_"+fieldCamelCaseName;

        return map.get(fieldCamelCaseName);
    }

    /**
     * Insert columns 필드리스트 기반 필드 리스트 생성 유틸
     * 컬럼 필드리스트 중 null, empty 조건에 해당되는 필드 값들을 리스트에 담아서 리턴해줌
     * 기본값으로 null 데이터를 허용 처리함
     * @param fieldArray
     * @param object
     * @return
     */
    public static List<Field> getColumnFieldList(Field<?>[] fieldArray, Object object){
        return getColumnFieldList(Lists.newArrayList(fieldArray), object);
    }

    /**
     * Insert columns 필드리스트 기반 필드 리스트 생성 유틸
     * 컬럼 필드리스트 중 null, empty 조건에 해당되는 필드 값들을 리스트에 담아서 리턴해줌
     * 기본값으로 null 데이터를 허용 처리함
     * @param fieldList
     * @param object
     * @return
     */
    public static<T> List<Field> getColumnFieldList(List<Field> fieldList, Object object) {
        return getColumnFieldList(fieldList, new ArrayList<>(), object, false, false);
    }

    /**
     * Insert columns 필드리스트 기반 필드 리스트 생성 유틸
     * 컬럼 필드리스트 중 null, empty 조건에 해당되는 필드 값들을 리스트에 담아서 리턴해줌
     * 기본값으로 null 데이터를 허용 처리함
     * @param fieldList
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: null 허용, false: null 미허용
     * @return
     */
    public static<T> List<Field> getColumnFieldList(List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption) {
        return getColumnFieldList(fieldList, new ArrayList<>(), object, nullOption, emptyOption);
    }

    /**
     * Insert columns 필드리스트 기반 필드 리스트 생성 유틸
     * 컬럼 필드리스트 중 null, empty 조건에 해당되는 필드 값들을 리스트에 담아서 리턴해줌
     * 기본값으로 null 데이터를 허용 처리함
     * @param fieldArray
     * @param object
     * @param addFieldLists 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @return
     */
    public static List<Field> getColumnFieldList(Field<?>[] fieldArray, Object object, Field<?> ... addFieldLists) {
        return getColumnFieldList(Lists.newArrayList(fieldArray), Lists.newArrayList(addFieldLists), object);
    }

    /**
     * Insert columns 필드리스트 기반 필드 리스트 생성 유틸
     * 컬럼 필드리스트 중 null, empty 조건에 해당되는 필드 값들을 리스트에 담아서 리턴해줌
     * 기본값으로 null 데이터를 허용 처리함
     * @param fieldList
     * @param object
     * @param addFieldLists 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @return
     */
    public static List<Field> getColumnFieldList(List<Field> fieldList, Object object, Field<?> ... addFieldLists) {
        return getColumnFieldList(fieldList, Lists.newArrayList(addFieldLists), object);
    }

    /**
     * Insert columns 필드리스트 기반 필드 리스트 생성 유틸
     * 컬럼 필드리스트 중 null, empty 조건에 해당되는 필드 값들을 리스트에 담아서 리턴해줌
     * 기본값으로 null 데이터를 허용 처리함
     * @param fieldArray
     * @param addFieldList 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @param object
     * @return
     */
    public static List<Field> getColumnFieldList(Field<?>[] fieldArray, List<Field> addFieldList, Object object) {
        return getColumnFieldList(Lists.newArrayList(fieldArray), addFieldList, object);
    }

    /**
     * Insert columns 필드리스트 기반 필드 리스트 생성 유틸
     * 컬럼 필드리스트 중 null, empty 조건에 해당되는 필드 값들을 리스트에 담아서 리턴해줌
     * 기본값으로 null 데이터를 허용 처리함
     * @param fieldList
     * @param addFieldList 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @param object
     * @return
     */
    public static List<Field> getColumnFieldList(List<Field> fieldList, List<Field> addFieldList, Object object) {
        return getColumnFieldList(fieldList, addFieldList, object, false, false);
    }

    /**
     * Insert columns 필드리스트 기반 필드 리스트 생성 유틸
     * 컬럼 필드리스트 중 null, empty 조건에 해당되는 필드 값들을 리스트에 담아서 리턴해줌
     * 기본값으로 null 데이터를 허용 처리함
     * @param fieldArray
     * @param addFieldList 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @param object
     * @return
     */
    public static List<Field> getColumnFieldList(Field<?>[] fieldArray, List<Field> addFieldList, Object object, boolean nullOption, boolean emptyOption) {
        return getColumnFieldList(Lists.newArrayList(fieldArray), addFieldList, object, nullOption, emptyOption);
    }

    /**
     * Insert columns 필드리스트 기반 필드 리스트 생성 유틸
     * 컬럼 필드리스트 중 null, empty 조건에 해당되는 필드 값들을 리스트에 담아서 리턴해줌
     * @param fieldArray
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: null 허용, false: null 미허용
     * @param addFieldLists 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @return
     */
    public static List<Field> getColumnFieldList(Field<?>[] fieldArray, Object object, boolean nullOption, boolean emptyOption, Field<?> ... addFieldLists) {
        return getColumnFieldList(Lists.newArrayList(fieldArray), Lists.newArrayList(addFieldLists), object, nullOption, emptyOption);
    }

    /**
     * Insert columns 필드리스트 기반 필드 리스트 생성 유틸
     * 컬럼 필드리스트 중 null, empty 조건에 해당되는 필드 값들을 리스트에 담아서 리턴해줌
     * @param fieldList
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: null 허용, false: null 미허용
     * @param addFieldLists 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @return
     */
    public static List<Field> getColumnFieldList(List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption, Field<?> ... addFieldLists) {
        return getColumnFieldList(fieldList, Lists.newArrayList(addFieldLists), object, nullOption, emptyOption);
    }

    /**
     * Insert columns 필드리스트 기반 필드 리스트 생성 유틸
     * 컬럼 필드리스트 중 null, empty 조건에 해당되는 필드 값들을 리스트에 담아서 리턴해줌
     * @param fieldList
     * @param addFieldList 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: null 허용, false: null 미허용
     * @return
     */
    public static List<Field> getColumnFieldList(List<Field> fieldList, List<Field> addFieldList, Object object, boolean nullOption, boolean emptyOption) {
        if(object == null) return null;
        if(object instanceof List) return null;

        List<Field> resultList = new ArrayList<>();

        if(fieldList != null && Objects.nonNull(object)) {
            for (Field field : fieldList) {
                Object objectValue = CommonUtils.strEmptyIfNull(emptyOption, getObjectValue(field, object));

                if (CommonUtils.isProcess(nullOption, objectValue))
                    resultList.add(field);
            }
        }

        // addField 가 있고 resultList에 있으면 제거 후 add
        if(!ListUtils.emptyIfNull(addFieldList).isEmpty()) {
            for (Field addField : addFieldList) {
                if(resultList.contains(addField))
                    resultList.remove(addField);
                resultList.add(addField);
            }
        }

        return resultList;
    }

    /**
     * 필드리스트 셋팅 후 가져오기
     * @return
     */
    public static List<Field> getFieldList(Field<?>[] fieldArray){
        return getFieldList(Lists.newArrayList(fieldArray), new ArrayList<>());
    }

    /**
     * 필드리스트 셋팅 후 가져오기
     * @return
     */
    public static List<Field> getFieldList(Field<?>[] fieldArray, Field<?> ... fields){
        return getFieldList(Lists.newArrayList(fieldArray), Lists.newArrayList(fields));
    }

    /**
     * 필드리스트 셋팅 후 가져오기
     * @return
     */
    public static List<Field> getFieldList(Field<?>[] fieldArray, List<Field> outList){
        return getFieldList(Lists.newArrayList(fieldArray), outList);
    }

    /**
     * 필드리스트 셋팅 후 가져오기
     * @return
     */
    public static List<Field> getFieldList(List<Field> inList){
        return getFieldList(inList, new ArrayList<>());
    }

    /**
     * 필드리스트 셋팅 후 가져오기
     * @return
     */
    public static List<Field> getFieldList(List<Field> inList, Field<?> ... fields){
        return getFieldList(inList, Lists.newArrayList(fields));
    }

    /**
     * 필드리스트 셋팅 후 제외리스트 제거 후 가져오기
     * @param inList
     * @param outList
     * @return
     */
    public static List<Field> getFieldList(List<Field> inList, List<Field> outList) {
        if(ListUtils.emptyIfNull(inList).isEmpty()) return null;

        List<Field> fieldList = new ArrayList<>();
        fieldList.addAll(inList);

        if(!ListUtils.emptyIfNull(outList).isEmpty())
            fieldList.removeAll(outList);
        return fieldList;
    }

    /**
     * 기존 필드리스트에 필드리스트 추가하기
     * @param fieldArray
     * @param addFieldLists
     * @return
     */
    public static List<Field> addAllFieldList(Field<?>[] fieldArray, Field<?> ... addFieldLists) {
        return addAllFieldList(Lists.newArrayList(fieldArray), Lists.newArrayList(addFieldLists));
    }

    /**
     * 기존 필드리스트에 필드리스트 추가하기
     * @param fieldArray
     * @param add
     * @return
     */
    public static List<Field> addAllFieldList(Field<?>[] fieldArray, List<Field> add) {
        return addAllFieldList(Lists.newArrayList(fieldArray), add);
    }

    /**
     * 기존 필드리스트에 필드리스트 추가하기
     * @param original
     * @param addFieldLists
     * @return
     */
    public static List<Field> addAllFieldList(List<Field> original, Field<?> ... addFieldLists) {
        return addAllFieldList(original, Lists.newArrayList(addFieldLists));
    }

    /**
     * 기존 필드리스트에 필드리스트 추가하기
     * @param original
     * @param add
     * @return
     */
    public static List<Field> addAllFieldList(List<Field> original, List<Field> add) {
        original = ListUtils.emptyIfNull(original);
        add = ListUtils.emptyIfNull(add);

        if(original.isEmpty() && add.isEmpty()) return null;

        List<Field> fieldList = new ArrayList<>();
        fieldList.addAll(original);
        fieldList.addAll(add);

        return fieldList;
    }


    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldArray Alias 로 적용하기 위한 Field List
     * @param object 적용할 object
     * @return
     */
    public static List<Field> setAliasFieldList(Field<?>[] fieldArray, Object object) {
        return setAliasFieldList(Lists.newArrayList(fieldArray), new ArrayList<>(), object, false, false, true);
    }

    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldList Alias 로 적용하기 위한 Field List
     * @param object 적용할 object
     * @return
     */
    public static List<Field> setAliasFieldList(List<Field> fieldList, Object object) {
        return setAliasFieldList(fieldList, new ArrayList<>(), object, false, false, true);
    }

    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldArray Alias 로 적용하기 위한 Field List
     * @param object 적용할 object
     * @param addFieldLists 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @return
     */
    public static List<Field> setAliasFieldList(Field<?>[] fieldArray, Object object, Field<?> ... addFieldLists) {
        return setAliasFieldList(Lists.newArrayList(fieldArray), Lists.newArrayList(addFieldLists), object);
    }

    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldArray Alias 로 적용하기 위한 Field List
     * @param addFieldList 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @param object 적용할 object
     * @return
     */
    public static List<Field> setAliasFieldList(Field<?>[] fieldArray, List<Field> addFieldList, Object object) {
        return setAliasFieldList(Lists.newArrayList(fieldArray), addFieldList, object);
    }

    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldList Alias 로 적용하기 위한 Field List
     * @param object 적용할 object
     * @param addFieldLists 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @return
     */
    public static List<Field> setAliasFieldList(List<Field> fieldList, Object object, Field<?> ... addFieldLists) {
        return setAliasFieldList(fieldList, Lists.newArrayList(addFieldLists), object);
    }

    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldList Alias 로 적용하기 위한 Field List
     * @param addFieldList 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @param object 적용할 object
     * @return
     */
    public static List<Field> setAliasFieldList(List<Field> fieldList, List<Field> addFieldList, Object object) {
        return setAliasFieldList(fieldList, addFieldList, object, false, false, true);
    }

    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldList Alias 로 적용하기 위한 Field List
     * @param object 적용할 object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: empty 허용, false: empty 미허용
     * @return
     */
    public static List<Field> setAliasFieldList(List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption) {
        return setAliasFieldList(fieldList, new ArrayList<>(), object, nullOption, emptyOption, true);
    }

    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldArray Alias 로 적용하기 위한 Field List
     * @param object 적용할 object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: empty 허용, false: empty 미허용
     * @return
     */
    public static List<Field> setAliasFieldList(Field<?>[] fieldArray, Object object, boolean nullOption, boolean emptyOption) {
        return setAliasFieldList(Lists.newArrayList(fieldArray), new ArrayList<>(), object, nullOption, emptyOption, true);
    }

    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldArray Alias 로 적용하기 위한 Field List
     * @param object 적용할 object
     * @param addFieldLists 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @return
     */
    public static List<Field> setAliasFieldList(Field<?>[] fieldArray, Object object, boolean nullOption, boolean emptyOption, Field<?> ... addFieldLists) {
        return setAliasFieldList(Lists.newArrayList(fieldArray), Lists.newArrayList(addFieldLists), object, nullOption, emptyOption, true);
    }

    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldArray Alias 로 적용하기 위한 Field List
     * @param addFieldList 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @param object 적용할 object
     * @return
     */
    public static List<Field> setAliasFieldList(Field<?>[] fieldArray, List<Field> addFieldList, Object object, boolean nullOption, boolean emptyOption) {
        return setAliasFieldList(Lists.newArrayList(fieldArray), addFieldList, object, nullOption, emptyOption, true);
    }

    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldList Alias 로 적용하기 위한 Field List
     * @param object 적용할 object
     * @param addFieldLists 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @return
     */
    public static List<Field> setAliasFieldList(List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption, Field<?> ... addFieldLists) {
        return setAliasFieldList(fieldList, Lists.newArrayList(addFieldLists), object, nullOption, emptyOption, true);
    }

    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldList Alias 로 적용하기 위한 Field List
     * @param addFieldList 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @param object 적용할 object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: empty 허용, false: empty 미허용
     * @return
     */
    public static List<Field> setAliasFieldList(List<Field> fieldList, List<Field> addFieldList, Object object, boolean nullOption, boolean emptyOption) {
        return setAliasFieldList(fieldList, addFieldList, object, nullOption, emptyOption, true);
    }

    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldArray Alias 로 적용하기 위한 Field List
     * @param object 적용할 object
     * @param addFieldLists 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @return
     */
    public static List<Field> setAliasFieldList(Field<?>[] fieldArray, Object object, boolean nullOption, boolean emptyOption, boolean isIpOption, Field<?> ... addFieldLists) {
        return setAliasFieldList(Lists.newArrayList(fieldArray), Lists.newArrayList(addFieldLists), object, nullOption, emptyOption, isIpOption);
    }

    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldArray Alias 로 적용하기 위한 Field List
     * @param addFieldList 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @param object 적용할 object
     * @return
     */
    public static List<Field> setAliasFieldList(Field<?>[] fieldArray, List<Field> addFieldList, Object object, boolean nullOption, boolean emptyOption, boolean isIpOption) {
        return setAliasFieldList(Lists.newArrayList(fieldArray), addFieldList, object, nullOption, emptyOption, isIpOption);
    }

    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldList Alias 로 적용하기 위한 Field List
     * @param object 적용할 object
     * @param addFieldLists 추가 할 리스트 기존 동일항목이 있으면 제거하고 뒤에 추가
     * @return
     */
    public static List<Field> setAliasFieldList(List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption, boolean isIpOption, Field<?> ... addFieldLists) {
        return setAliasFieldList(fieldList, Lists.newArrayList(addFieldLists), object, nullOption, emptyOption, isIpOption);
    }

    /**
     * Object 값을 DSL.value에 맵핑하고 Field Name 을 Alias 로 추가
     * insert select 등에서 사용하기 위해 셋팅하는 field List 유틸
     * @param fieldList Alias 로 적용하기 위한 Field List
     * @param object 적용할 object
     * @param nullOption true: null 허용, false: null 미허용
     * @param emptyOption true: empty 허용, false: empty 미허용
     * @param isIpOption true: IP 옵션 적용, false: IP 옵션 미적용
     * @return
     */
    public static List<Field> setAliasFieldList(List<Field> fieldList, List<Field> addFieldList, Object object, boolean nullOption, boolean emptyOption, boolean isIpOption ){

        List<Field> aliasFieldList = new ArrayList<>();

        if(fieldList != null && Objects.nonNull(object)) {
            for (Field field : fieldList) {
                Object objectValue = CommonUtils.strEmptyIfNull(emptyOption, getObjectValue(field, object));

                if (CommonUtils.isProcess(nullOption, objectValue)) {
                    Field objectField = DSL.value(objectValue).as(field.getName());
                    if(field.getName().toLowerCase().endsWith("ip") && isIpOption )
                        objectField = setConvertIp(String.valueOf(objectValue)).as(field.getName());
                    aliasFieldList.add(objectField);
                    /*
                    if (objectValue instanceof String) {
                        if (StringUtils.isNotEmpty((String) objectValue))
                            moreStep.set(field, objectValue);
                    } else moreStep.set(field, objectValue);
                    */
                }
            }
        }

        // addField 가 있고 resultList에 있으면 제거 후 add
        if(!ListUtils.emptyIfNull(addFieldList).isEmpty()) {
            for (Field addField : addFieldList) {

                if(aliasFieldList.stream().anyMatch(field -> field.getName().equals(addField.getName())))
                    aliasFieldList.removeIf(field -> field.getName().equals(addField.getName()));
                aliasFieldList.add(addField);
            }
        }

        return aliasFieldList;
    }

    /**
     * 날짜형식을 YYYY-MM-DD hh:mm:ss 로 표기
     * @param field
     * @return
     */
    public static Field<String> setDateTimeFormat(Field field){
        return setDateTimeFormat(field, null);
    }

    /**
     * 날짜형식을 입력 받은 포멧으로 표기
     * 기본값은 YYYY-MM-DD hh:mm:ss
     * @param field
     * @return
     */
    public static Field<String> setDateTimeFormat(Field field, String format){
        if(StringUtils.isEmpty(format)) format = "%Y-%m-%d %H:%i:%s";
        return DSL.function("DATE_FORMAT", String.class,field,value(format)).as(field.getName());
    }

    /**
     * 스키마명 과 테이블명 String 형태로 변환해서 가져오기
     * @param table
     * @param <T>
     * @return
     */
    public static <T extends Table> String getTableNmToString(T table) {
        return table.getSchema().getName()+"."+table.getName();
    }

    /**
     * 해당 테이블의 AUTO_INCREMENT 값 1로 초기화
     * @param dsl
     * @param table
     * @param <T>
     */
    public static <T extends Table> void resetAutoincrement(DSLContext dsl, T table) {
        resetAutoincrement(dsl, table, 1);
    }

    /**
     * 해당 테이블의 AUTO_INCREMENT 값 원하는 값으로 초기화
     * @param dsl
     * @param table
     * @param value 변경할 AUTO_INCREMENT 값
     * @param <T>
     */
    public static <T extends Table> void resetAutoincrement(DSLContext dsl, T table, Integer value) {
        dsl.execute("ALTER TABLE "+getTableNmToString(table)+" AUTO_INCREMENT = "+value);
    }

    /**
     * 해당 테이블 데이터를 초기화 하고 AUTO_INCREMENT 값을 1로 초기화 한다
     * @param dsl
     * @param table
     * @param <T>
     */
    public static <T extends Table> void resetTable(DSLContext dsl, T table) {
        dsl.deleteFrom(table).execute();
        JooqUtils.resetAutoincrement(dsl, table);
    }

    /**
     * IP를 int 바이너리 형태로 변환
     * @param ip
     * @return
     */
    public static Field<Integer> setConvertIp(String ip) {
        return DSL.function("INET_ATON", Integer.class, value(ip, String.class));
    }

    /**
     * 바이너리 형태로 변환된 IP주소를 ". . . ." 형태로 변환
     * @param field
     * @return
     */
    public static Field<String> getConvertIp(Field field) {
        return DSL.function("INET_NTOA", String.class,field).as(field.getName());
    }

    /**
     * 해당 테이블의 가장 마지막에 생성된 ID 조회
     * @param field
     * @param <T>
     * @return
     */
    public static Field<Integer> getLastInsertId(Field field) {
        return DSL.function("LAST_INSERT_ID", Integer.class, field);
    }

    /**
     * 테이블의 AutoIncrement 값 조회
     * @param dsl
     * @param table
     * @return
     */
    public static <T extends Table> int getAutoIncrementValue(DSLContext dsl, T table) {
        return dsl.select(DSL.field("AUTO_INCREMENT"))
                .from(INFORMATION_SCHEMA.TABLES)
                .where(DSL.field("table_name").eq(table.getName()))
                .and(DSL.field("table_schema").eq(table.getSchema().getName()))
                .fetchOneInto(Integer.class);
    }

    /**
     * MySQL 함수인 SUBSTRING_INDEX
     * 1번째 인수 : 컬럼명
     * 2번째 인수 : 기준문자, 구분 문자
     * 3번째 인수 : 보여질 위치, 위치 기준 음수(-) 1부터는 뒷부분 부터, 양수(+) 1부터는 앞부분 부터
     * @param field
     * @param splitStr
     * @param index
     * @param <T>
     * @return
     */
    public static<T extends Field<String>> Field<String> funcSubStringIndex(T field, String splitStr, Integer index) {
        return DSL.function("SUBSTRING_INDEX", String.class, field, DSL.val(splitStr, String.class), DSL.val(index, Integer.class));
    }

    public static Field<String> funcZerofill(Field field, Integer length) {
        return DSL.function("LPAD", String.class, field, DSL.val(length, Integer.class), value("0")).as(field.getName());
    }

    /**
     * 테이블 필드를 오브젝트 객체 리스트로 반환
     * 테이블 필드를 카멜케이스로 변환하고 주석 코멘트를 추가하여 객체 리스트로 반환
     * @param table Jooq 테이블 객체
     * @return List<String>
     */
    public static List<String> makeObject(Table<?> table) {
        return makeObject(table, false);
    }

    /**
     * 테이블 필드를 오브젝트 객체 리스트로 반환
     * 테이블 필드를 카멜케이스로 변환하고 주석 코멘트를 추가하여 객체 리스트로 반환
     * @param table Jooq 테이블 객체
     * @param apiModel ApiModelProperty 생성여부
     * @return List<String>
     */
    public static List<String> makeObject(Table<?> table, boolean apiModel) {
        List<String> objectList = new ArrayList<>();

        if(Objects.isNull(table)) return objectList;

        objectList.add("테이블 코멘트: "+(apiModel ? "@ApiModel(\""+table.getComment()+"\")" : table.getComment()));
        objectList.add("테이블 명: "+ CommonUtils.toPascalCase(table.getName()));
        objectList.add("");

        String typeName;

        for(Field<?> field : table.fields()) {
            objectList.add(apiModel ? "@ApiModelProperty(\""+field.getComment()+"\")" : "/** "+field.getComment() +" **/");

            typeName = field.getDataType().getType().getSimpleName();
            //log.debug("## DataType = {}, type = {}, typeName = {}",field.getDataType().getTypeName(),field.getDataType().getType(),typeName);
            // tinyint(Byte) 타입은 Boolean 형으로
            if(field.getDataType().getType() == Byte.class) typeName = Boolean.class.getSimpleName();
                // BigInt(Long) 타입은 Long 형으로
            else if(field.getDataType().getType() == Long.class) typeName = Long.class.getSimpleName();
                // 그외 나머지 Number 타입은 Integer 형으로
            else if(field.getDataType().getType().getSuperclass() == Number.class) typeName = Integer.class.getSimpleName();

            objectList.add("private "+typeName+" "+ CommonUtils.toCamelCase(field.getName())+";");
        }

        return objectList;
    }
}