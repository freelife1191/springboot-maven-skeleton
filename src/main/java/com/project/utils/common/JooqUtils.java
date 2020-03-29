package com.project.utils.common;

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

import static com.project.utils.common.constant.ConditionType.eq;
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
     * 특정 FieldList 제외해야할 경우
     * List<Condition> conditionsList = setConditionList(fieldList, object, outList);
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

    public static List<Condition> setConditionList(List<Field> fieldList, Object object, List<Field> outList) {
        return setConditionList(null, fieldList, object, outList, eq);
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
        return setConditionList(null, fieldList, object, null, eq, nullOption, emptyOption);
    }

    public static List<Condition> setConditionList(List<Field> fieldList, Object object, List<Field> outList, boolean nullOption, boolean emptyOption) {
        return setConditionList(null, fieldList, object, outList, eq, nullOption, emptyOption);
    }

    public static List<Condition> setConditionList(List<Condition> conditionList, List<Field> fieldList, Object object) {
        return setConditionList(conditionList, fieldList, object, eq);
    }

    public static List<Condition> setConditionList(List<Condition> conditionList, List<Field> fieldList, Object object, List<Field> outList) {
        return setConditionList(conditionList, fieldList, object, outList, eq);
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
        return setConditionList(conditionList, fieldList, object, null, eq, nullOption, emptyOption);
    }

    public static List<Condition> setConditionList(List<Condition> conditionList, List<Field> fieldList, List<Field> outList, Object object, boolean nullOption, boolean emptyOption) {
        return setConditionList(conditionList, fieldList, object, outList, eq, nullOption, emptyOption);
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
     * 특정 FieldList 제외해야할 경우
     * List<Condition> conditionsList = setConditionList(conditionList, fieldList, object, outList, conditionType);
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
        return setConditionList(conditionList, fieldList, object, null, conditionType, false, false);
    }

    public static List<Condition> setConditionList(List<Condition> conditionList, List<Field> fieldList, Object object, List<Field> outList, ConditionType conditionType) {
        return setConditionList(conditionList, fieldList, object, outList, conditionType, false, false);
    }

    /**
     * 기존 conditionList와 TableField 값과 Object 를 받아 ConditionList를 셋팅함
     * 객체와 조건을 추가할 TableFieldList를 함께 넘겨 null이 아닌 값은 자동으로 조건에 추가하는 함수
     *
     * Select Where 조건에 해당되는 TableField 리스트를 생성
     * List<TableField> fieldList = new ArrayList<>();
     * fieldList.add(SERVICE_DETAIL_CODE.SERVICE_CODE);`
     *
     * List<Condition> conditionsList = setConditionList(conditionList, fieldList, object, outList, conditionType, nullOption, emptyOption);
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
    public static List<Condition> setConditionList(List<Condition> conditionList, List<Field> fieldList, Object object, List<Field> outList, ConditionType conditionType, boolean nullOption, boolean emptyOption) {

        if(conditionList == null) conditionList = new ArrayList<>();

        if(!ListUtils.emptyIfNull(outList).isEmpty())
            fieldList = getFieldList(fieldList, outList);

        if(fieldList != null && Objects.nonNull(object)) {
            for (Field field : fieldList) {
                Map<String, Object> map = CommonUtils.objectToMap(object);
                String fieldCamelCaseName = CaseUtils.toCamelCase(field.getName(), false, new char[]{'_'});

                Object conditionData = map.get(fieldCamelCaseName);
                //프로세스 진행여부 판단 후 진행
                if (CommonUtils.isProcess(nullOption, emptyOption, conditionData)) {

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
     * nullOption false: null 미허용 기본값
     * @param insert
     * @param fieldList
     * @param object
     * @return
     */
    public static InsertSetMoreStep setInsertSetMoreStep(InsertSetStep insert, List<Field> fieldList, Object object){
        InsertSetMoreStep moreStep = (InsertSetMoreStep) insert;
        return setInsertSetMoreStep(moreStep, fieldList, object, true, false);
    }

    /**
     * Object의 null이 아닌 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 InsertSetMoreStep에 에 추가 적용
     * @param insert
     * @param fieldList
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @return
     */
    public static InsertSetMoreStep setInsertSetMoreStep(InsertSetStep insert, List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption){
        InsertSetMoreStep moreStep = (InsertSetMoreStep) insert;
        return setInsertSetMoreStep(moreStep, fieldList, object, nullOption, emptyOption);
    }

    /**
     * Object의 null이 아닌 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 InsertSetMoreStep에 에 추가 적용
     * nullOption false: null 미허용 기본값
     * @param moreStep
     * @param fieldList
     * @param object
     * @return
     */
    public static InsertSetMoreStep setInsertSetMoreStep(InsertSetMoreStep moreStep, List<Field> fieldList, Object object){
        return setInsertSetMoreStep(moreStep, fieldList, object, true, false);
    }

    /**
     * Object의 null이 아닌 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 InsertSetMoreStep에 에 추가 적용
     * @param moreStep
     * @param fieldList
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @return
     */
    public static InsertSetMoreStep setInsertSetMoreStep(InsertSetMoreStep moreStep, List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption){

        if(moreStep == null) return null;

        if(fieldList != null && Objects.nonNull(object)) {
            for (Field field : fieldList) {
                Object objectValue = getObjectValue(field, object);

                if (CommonUtils.isProcess(nullOption, objectValue)) {
                    moreStep.set(field, CommonUtils.strEmptyIfNull(emptyOption, objectValue));
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
        UpdateSetMoreStep moreStep = (UpdateSetMoreStep) update;
        return setUpdateSetMoreStep(moreStep, fieldList, object, nullOption, emptyOption);
    }

    /**
     * Object의 값을 fieldlist의 field 들에 셋팅하여
     * 넘겨 받은 UpdateSetMoreStep 에 추가 적용
     * nullOption false: null 미허용 기본값
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
     * @return
     */
    public static UpdateSetMoreStep setUpdateSetMoreStep(UpdateSetMoreStep moreStep, List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption){

        if(moreStep == null) return null;

        if(fieldList != null && Objects.nonNull(object)) {
            for (Field field : fieldList) {
                Object objectValue = getObjectValue(field, object);

                if (CommonUtils.isProcess(nullOption, objectValue)) {
                    moreStep.set(field, CommonUtils.strEmptyIfNull(emptyOption, objectValue));
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
        return getObjectValueList(fieldList, object, true, false);
    }

    /**
     * Insert Values 필드리스트 기반 오브젝트 값 리스트 생성 유틸
     * 필드리스트에 해당되는 오브젝트의 값들을 리스트에 담아서 리턴해줌
     * @param fieldList
     * @param object
     * @param nullOption true: null 허용, false: null 미허용
     * @return
     */
    public static List<Object> getObjectValueList(List<Field> fieldList, Object object, boolean nullOption, boolean emptyOption) {
        if(object == null) return null;

        List<Object> resultList = new ArrayList<>();

        if(fieldList != null && Objects.nonNull(object)) {
            for (Field field : fieldList) {
                Object objectValue = getObjectValue(field, object);

                if (CommonUtils.isProcess(nullOption, objectValue)) {
                    resultList.add(CommonUtils.strEmptyIfNull(emptyOption, objectValue));
                }
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
        return map.get(fieldCamelCaseName);
    }

    /**
     * 필드리스트 셋팅 후 가져오기
     * @return
     */
    public static List<Field> getFieldList(List<Field> inList){
        return getFieldList(inList, null);
    }

    /**
     * 필드리스트 셋팅 후 제외리스트 제거 후 가져오기
     * @param outList
     * @return
     */
    public static List<Field> getFieldList(List<Field> inList, List<Field> outList) {
        if(inList == null) return null;

        List<Field> fieldList = new ArrayList<>();
        fieldList.addAll(inList);

        if(outList != null)
            fieldList.removeAll(outList);
        return fieldList;
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
        return DSL.function("DATE_FORMAT",String.class,field,value(format)).as(field.getName());
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

}