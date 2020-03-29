package com.project.utils.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class CommonUtils {

    /**
     * Object 필드만큼 루프 돌면서 Field 맵을 생성함
     * @param object
     * @return
     */
    private static Map getResultMap(Object object){
        Map resultMap = new LinkedHashMap<>();

        // 기본 클래스 값 셋팅
        for(Field field : object.getClass().getDeclaredFields())
            resultMap.put(field.getName(), getFieldObject(field, object));

        // 슈퍼 클래스 값 셋팅
        for(Field field : object.getClass().getSuperclass().getDeclaredFields())
            resultMap.put(field.getName(), getFieldObject(field, object));

        return resultMap;
    }



    /**
     * Field 허용 처리 및 Object 값 받아오기
     * @param field
     * @param object
     * @return
     */
    public static Object getFieldObject(Field field, Object object) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }
    /**
     * Object를 Map으로 변환
     * @param object
     * @return
     *
     * @throws IllegalAccessException
     */
    public static Map objectToMap(Object object) {
        return getResultMap(object);
    }

    /**
     * Object를 Map으로 변환
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, String> objectToMapString(Object object) {

        Map<String, String> resultMap = new HashMap<>();
        for(Field field : object.getClass().getDeclaredFields()) {
            Object fieldObject = getFieldObject(field, object);
            if(fieldObject.getClass().getTypeName().startsWith("java.lang") ||
                    fieldObject.getClass().getTypeName().startsWith("java.time")) {
                resultMap.put(field.getName(), String.valueOf(fieldObject));
            }else{
                for (Field subField : fieldObject.getClass().getDeclaredFields()) {
                    Object subFieldObject = getFieldObject(subField, fieldObject);
                    resultMap.put(field.getName()+"."+subField.getName(), String.valueOf(subFieldObject));
                }
            }
        }

        for(Field field : object.getClass().getSuperclass().getDeclaredFields()) {
            Object fieldObject = getFieldObject(field, object);
            if(fieldObject.getClass().getTypeName().startsWith("java.lang") ||
                    fieldObject.getClass().getTypeName().startsWith("java.time")) {
                resultMap.put(field.getName(), String.valueOf(fieldObject));
            }else{
                for (Field subField : fieldObject.getClass().getDeclaredFields()) {
                    Object subFieldObject = getFieldObject(subField, fieldObject);
                    resultMap.put(field.getName()+"."+subField.getName(), String.valueOf(subFieldObject));
                }
            }
        }
        return resultMap;
    }

    /**
     * Object에 해당 필드명에 해당되는 값을 가져옴
     * myAddress.country 형태의 sub 필드까지 가능함
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    public static Object getObjectValue(Object object, String fieldName) {

        if(Objects.isNull(object) && StringUtils.isEmpty(fieldName)) return null;

        String arrfieldName = null;
        String subFieldName = null;

        if(fieldName.contains(".")) {
            String[] fieldNameArray = fieldName.split("\\.");
            arrfieldName = fieldNameArray[0];
            subFieldName = fieldNameArray[1];
        }

        for (Field field : object.getClass().getDeclaredFields()) {
            Object fieldObject = getFieldObject(field, object);
            if(!StringUtils.isEmpty(arrfieldName) && field.getName().equals(arrfieldName))
                return getFieldNameEqualsObject(fieldObject, subFieldName);
            else if (field.getName().equals(fieldName))
                return fieldObject;
        }
        for (Field field : object.getClass().getSuperclass().getDeclaredFields()) {
            Object fieldObject = getFieldObject(field, object);
            if(!StringUtils.isEmpty(arrfieldName) && field.getName().equals(arrfieldName))
                return getFieldNameEqualsObject(fieldObject, subFieldName);
            else if (field.getName().equals(fieldName))
                return fieldObject;
        }
        return null;
    }

    /**
     * Object의 특정 필드명과 같은 Object 값을 리턴
     * @param object
     * @param fieldName
     * @return
     */
    private static Object getFieldNameEqualsObject(Object object, String fieldName) {

        for (Field field : object.getClass().getDeclaredFields()) {
            if(field.getName().equals(fieldName))
                return getFieldObject(field, object);
        }
        for (Field field : object.getClass().getSuperclass().getDeclaredFields()) {
            if(field.getName().equals(fieldName))
                return getFieldObject(field, object);
        }
        return null;
    }

    /**
     * Object Value To List
     * @param object
     * @return
     */
    public static List<Object> objectValueToList(Object object) {
        return mapValueToList(objectToMap(object));
    }

    /**
     * Map Value To List
     * @param map
     * @return
     */
    public static List<Object> mapValueToList(Map<String, Object> map) {
        return map.values().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Map Key To List
     * @param map
     * @return
     */
    public static List<String> mapKeyToList(Map<String, Object> map) {
        return map.keySet().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 객체의 데이터가 있는지 확인
     * 있으면 true 없으면 false
     * @param object
     * @return
     */
    public static boolean objectNotEmpty(Object object){
        return Objects.nonNull(object) && !objectEmpty(object);
    }

    /**
     * 객체의 데이터가 없는지 확인
     * 없으면 true 있으면 false
     * @param object
     * @return
     */
    public static boolean objectEmpty(Object object) {
        return Objects.isNull(object) || CommonUtils.objectValueToList(object).isEmpty();
    }


    /**
     * 구분자로 문자데이터를 추가 연결함 (기본값 '|' )
     * @param objectList 데이터 리스트
     * @return
     */
    public static String getJoinData(List<Object> objectList) {
        return getJoinData((String) null, objectList);
    }

    /**
     * 구분자로 문자데이터를 추가 연결함 (기본값 '|' )
     * @param joinStr 구분자
     * @param objectList 데이터 리스트
     * @return
     */
    public static String getJoinData(String joinStr, List<Object> objectList) {

        StringJoiner joiner;

        if(StringUtils.isNotEmpty(joinStr)) joiner = new StringJoiner(joinStr);
        else joiner = new StringJoiner("|");

        return getJoinData(joiner, objectList);

    }

    /**
     * 구분자로 문자데이터를 추가 연결함 (기본값 '|' )
     * @param joiner 구분 Joiner
     * @param objectList 데이터 리스트
     * @return
     */
    public static String getJoinData(StringJoiner joiner, List<Object> objectList) {

        if(Objects.isNull(joiner)) joiner = new StringJoiner("|");

        for(Object object : objectList) {
            joiner.add(String.valueOf(object));
        }
        return joiner.toString();
    }

    /**
     * target 문자열에서 character 문자열을 빈값으로 replaceAll 변환
     * @param target
     * @param character
     * @return
     */
    public static String getReplaceAll(String target, String character) {
        return getReplaceAll(target, character, "");
    }

    /**
     * target 문자열에서 character 문자열을  replacement로 replaceAll 변환
     * @param target
     * @param character
     * @param replacement
     * @return
     */
    public static String getReplaceAll(String target, String character, String replacement) {
        return target != null ? target.replaceAll(character,replacement).trim() : null;
    }

    /**
     * target 문자열에서 character 문자열을 빈값으로 replace 변환
     * @param target
     * @param character
     * @return
     */
    public static String getReplace(String target, String character) {
        return getReplace(target, character, "");
    }

    /**
     * target 문자열에서 character 문자열을  replacement로 replace 변환
     * @param target
     * @param character
     * @return
     */
    public static String getReplace(String target, String character, String replacement) {
        return target != null ? target.replace(character,replacement).trim() : null;
    }

    /**
     * CamelCase To SnakeCase
     * @param word
     * @return
     */
    public static String toSnakeCase(String word){
        //카멜케이스를 스네이크 케이스로 변환
        String regex = "([a-z])([A-Z]+)"; String replacement = "$1_$2";
        return word.replaceAll(regex, replacement).toLowerCase();
    }

    /**
     * DB 데이터 변경 확인
     * true : 실패
     * false : 성공
     * @param executeData
     * @return
     */
    public static boolean isChangedData(int executeData) {
        return executeData <= 0;
    }

    /**
     * 객체의 데이터가 null 이거나 빈값인지 확인
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
        if (object == null)  return true;
        if ((object instanceof String) && (((String)object).trim().length() == 0))  return true;
        if (object instanceof Map)  return ((Map<?, ?>)object).isEmpty();
        if (object instanceof List)  return ((List<?>)object).isEmpty();
        if (object instanceof Object[])  return (((Object[])object).length == 0);
        return false;
    }

    /**
     * null 데이터와 empty 데이터인 경우 프로세스 진행여부 판단 유틸
     * nullOption(null 허용옵션), emptyOption( String 빈값 허용옵션 ), 판단할 object 값
     * @param nullOption nullOption null 값 허용(true), 미허용(false) 기본값 미허용(false)
     * @param emptyOption String Empty 값 허용(true), 미허용(false) 기본값 미허용(false)
     * @return
     */
    public static boolean isProcess(boolean nullOption, boolean emptyOption, Object object) {
        // empty 진행여부
        boolean emptyProcess = true;

        if( Objects.nonNull(object) && object instanceof String && !emptyOption) {
            String conditionStrData = String.valueOf(object).trim();
            // empty 데이터가 true 이면 empty 데이터 검색 허용
            // 기본값은 false empty 값 미허용
            emptyProcess = StringUtils.isNotEmpty(conditionStrData);
        }

        return isProcess(nullOption, object) && emptyProcess;
    }

    /**
     * null 데이터인 경우 프로세스 진행여부 판단 유틸
     * @param nullOption nullOption null 값 허용(true), 미허용(false) 기본값 미허용(false)
     * @param object 판단할 object 값
     * @return
     */
    public static boolean isProcess(boolean nullOption, Object object) {
        // null 진행여부
        boolean nullProcess = true;

        // nullOption 이 미허용(false) 인 경우 null이 아닌 데이터만 conditionList에 포함
        if(!nullOption)
            nullProcess = object != null;

        return nullProcess;
    }

    /**
     * String Empty 값 null 변환 유틸
     * String 문자열이 empty 값이면 null 로 변환하여 리턴한다
     * @param object 변환할 값
     * @return 원래 값 또는 변환된 값
     */
    public static Object strEmptyIfNull(Object object) {
        return strEmptyIfNull(false, object);
    }
    /**
     * String Empty 값 null 변환 유틸
     * String 문자열이 empty 값이면 null 로 변환하여 리턴한다
     * @param emptyOption true: null 변환 X, false: null 변환처리
     * @param object 변환할 값
     * @return 원래 값 또는 변환된 값
     */
    public static Object strEmptyIfNull(boolean emptyOption, Object object) {

        if(Objects.nonNull(object) && !emptyOption && object instanceof String && StringUtils.isEmpty(((String) object).trim()))
            object = null;

        return object;

    }

}