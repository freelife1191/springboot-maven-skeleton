package com.project.utils.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 테스트에서 사용하기 위한 유틸리티
 * Created by KMS on 12/03/2020.
 */
@Slf4j
public class TestUtils {
    /**
     * Sub Object 가 있다면 depth 만큼 더 돌면서 resultMap에 String 값을 추가
     * @param object
     * @param field
     * @param resultMap
     * @param prefix
     * @return
     */
    private static Map<String, String> getSubObjectStrMap(Object object, Field field, Map<String, String> resultMap, String prefix) {
        if (Objects.nonNull(object)) {

            boolean isEnum;
            try {
                isEnum = Class.forName(field.getType().getTypeName()).isEnum();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e.getMessage(), e);
            }

            if(field.getType().getTypeName().startsWith("java") || isEnum) {
                String fieldName = StringUtils.isNotEmpty(prefix) ? prefix + field.getName() : field.getName();
                resultMap.put(fieldName, String.valueOf(object));
            }
            else resultMap = getResultStrMap(object, resultMap, field.getName()+".");
        }
        return resultMap;
    }

    /**
     * Object 필드만큼 루프 돌면서 Map<String, String> Field 맵을 생성함
     * @param object
     * @return
     */
    private static Map<String, String> getResultStrMap(Object object){
        return getResultStrMap(object, null, null);
    }

    /**
     * Object 필드만큼 루프 돌면서 Map<String, String> Field 맵을 생성함
     * @param object
     * @return
     */
    private static Map<String, String> getResultStrMap(Object object, Map<String, String> resultMap, String prefix){

        if(Objects.isNull(resultMap))
            resultMap = new LinkedHashMap<>();

        // 기본 클래스 값 셋팅
        for(Field field : object.getClass().getDeclaredFields())
            resultMap = getSubObjectStrMap(CommonUtils.getFieldObject(field, object), field, resultMap, prefix);
        // 슈퍼 클래스 값 셋팅
        for(Field field : object.getClass().getSuperclass().getDeclaredFields())
            resultMap = getSubObjectStrMap(CommonUtils.getFieldObject(field, object), field, resultMap, prefix);

        return resultMap;
    }

    /**
     * Object를 Map<String, String>으로 변환
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, String> objectToStrMap(Object object) {
        return getResultStrMap(object);
    }

    /**
     * 테스트 코드에서 GET 타입을 테스트할때 Object를 MultiValueMap 타입으로 변경해줌
     *
     *
     * 예시)
     * mockMvc.perform(get("/v1/api")
     *         .contentType(MediaType.APPLICATION_JSON)
     *         .params(CommonUtils.objectToMultiValueMap(object)))
     *         .andExpect(status().isOk())
     *         .andDo(print())
     *         .andExpect(jsonPath("$.code", is(equalTo(100))))
     *         .andExpect(jsonPath("result").exists());
     *
     * @param object
     * @return
     */
    public static MultiValueMap<String, String> objectToMultiValueMap(Object object) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.setAll(objectToStrMap(object));
        return multiValueMap;
    }
}
