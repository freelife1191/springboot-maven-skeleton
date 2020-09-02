package com.project.utils.common;

import com.google.common.collect.Lists;
import com.google.gson.internal.Primitives;
import com.project.exception.common.NotSupportedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.project.exception.common.constant.CommonError.NotSupportedException;

/**
 * 테스트에서 사용하기 위한 유틸리티
 * Created by KMS on 12/03/2020.
 */
@Slf4j
public class TestUtils {

    public static final String SNAKE = "snake";
    public static final String CAMEL = "camel";

    /**
     * Sub Object 가 있다면 depth 만큼 더 돌면서 resultMap에 String 값을 추가
     * @param object
     * @param field
     * @param resultMap
     * @param prefix
     * @return
     */
    private static Map<String, Object> getSubObjectMap(Object object, Field field, Map<String, Object> resultMap, String prefix, String caseStrategy) {
        if (Objects.nonNull(object)) {

            boolean isEnum = false;

            try {
                if(!Primitives.isPrimitive(field.getType()))
                    isEnum = Class.forName(field.getType().getTypeName()).isEnum();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e.getMessage(), e);
            }

            ArrayList<Boolean> allowFieldTypes = Lists.newArrayList(
                    field.getType().getTypeName().startsWith("java"),
                    Primitives.isPrimitive(field.getType()),
                    isEnum
            );

            if(allowFieldTypes.stream().noneMatch(data -> data))
                throw new NotSupportedException("TestUtils에서 지원되지 않는 Field Type 추가 요망 :: FieldName = "+field.getName()+", FieldType = "+field.getType().getTypeName());

            if(allowFieldTypes.stream().anyMatch(data -> data)) {
                String fieldName = StringUtils.isNotEmpty(prefix) ? prefix + field.getName() : field.getName();
                if(StringUtils.isNotEmpty(caseStrategy)) {
                    switch (caseStrategy){
                        case SNAKE -> fieldName = CommonUtils.toSnakeCase(fieldName);
                        case CAMEL -> fieldName = CommonUtils.toCamelCase(fieldName);
                        default -> throw new IllegalStateException("Unexpected value: " + caseStrategy);
                    }
                }
                resultMap.put(fieldName, object);
            }
            else resultMap = getResultMap(object, resultMap, field.getName()+".", caseStrategy);
        }
        return resultMap;
    }

    /**
     * Object 필드만큼 루프 돌면서 Map<String, String> Field 맵을 생성함
     * @param object
     * @return
     */
    private static Map<String, Object> getResultMap(Object object, String prefix, String caseStrategy){
        return getResultMap(object, null, prefix, caseStrategy);
    }

    /**
     * Object 필드만큼 루프 돌면서 Map<String, String> Field 맵을 생성함
     * @param object
     * @return
     */
    private static Map<String, Object> getResultMap(Object object, Map<String, Object> resultMap, String prefix, String caseStrategy){

        if(Objects.isNull(resultMap))
            resultMap = new LinkedHashMap<>();

        if(object instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) object;
            for (String key: map.keySet())
                resultMap.put(key, map.get(key));
            return resultMap;
        }

        // 기본 클래스 값 셋팅
        for(Field field : object.getClass().getDeclaredFields())
            resultMap = getSubObjectMap(CommonUtils.getFieldObject(field, object), field, resultMap, prefix, caseStrategy);
        int i = 0;
        // 슈퍼 클래스 값이 있을 때까지 반복
        Class<?> superClassMapping = CommonUtils.getSuperClassMapping(object.getClass());
        while (Objects.nonNull(superClassMapping)) {
            for(Field field : superClassMapping.getDeclaredFields())
                resultMap = getSubObjectMap(CommonUtils.getFieldObject(field, object), field, resultMap, prefix, caseStrategy);
            superClassMapping = CommonUtils.getSuperClassMapping(superClassMapping);
            if( i >= 100) break;
            i++;
        }

        return resultMap;
    }

    /**
     * Object를 Map<String, String>으로 변환
     * @param object
     * @param caseStrategy
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object object, String prefix, String caseStrategy) {
        return getResultMap(object, prefix, caseStrategy);
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
     * @param object MultiValueMap으로 변환할 오브젝트
     * @return
     */
    public static MultiValueMap<String, String> objectToMultiValueMap(Object object) {
        return objectToMultiValueMap(object, null, null);
    }

    /**
     * 테스트 코드에서 GET 타입을 테스트할때 Object를 MultiValueMap 타입으로 변경해줌 (prefix)
     *
     * 이런식으로 prefix 값을 전달하면 GET 요청시 Parameter 변수에 prefix를 붙여서 셋팅함
     * params(TestUtils.objectToMultiValueMap(condition, "condition.")
     * condition.pklpChargeType=[2], condition.pklpLimitType=[4], condition.pklpLimitValue=[10], condition.pklpVehicleType=[A]}
     *
     * 예시)
     * mockMvc.perform(get("/v1/api")
     *         .contentType(MediaType.APPLICATION_JSON)
     *         .params(TestUtils.objectToMultiValueMap(object))
     *         .params(TestUtils.objectToMultiValueMap(condition, "condition.")))
     *         .andExpect(status().isOk())
     *         .andDo(print())
     *         .andExpect(jsonPath("$.code", is(equalTo(100))))
     *         .andExpect(jsonPath("result").exists());
     *
     * @param object MultiValueMap으로 변환할 오브젝트
     * @param prefix Parameter 앞에 붙일 String 데이터
     * @param caseStrategy 파라메터 Strategy
     * @return
     */
    public static MultiValueMap<String, String> objectToMultiValueMap(Object object, String prefix, String caseStrategy) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        Map<String, Object> objectMap = objectToMap(object, prefix, caseStrategy);
        for (String key : objectMap.keySet()) {
            Object value = objectMap.get(key);
            if (value instanceof List) {
                List<String> list = (List<String>) ((List) value).stream()
                        .map(data -> String.valueOf(data))
                        .collect(Collectors.toList());
                multiValueMap.put(key, list);
            }
            else if(value instanceof Map)
                multiValueMap.set(key, JsonUtils.toMapperJson(value));
            else
                multiValueMap.set(key, String.valueOf(value));
        }

        return multiValueMap;
    }

    public static MultiValueMap<String, String> objectToMultiValueMap(Object object, String prefix) {
        return objectToMultiValueMap(object, prefix, null);
    }
}
