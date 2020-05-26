/**
 * JsonUtils.java $version 2017. 7. 18.
 * <p>
 * Copyright 2017 Foodtechkorea Corp. All rights Reserved.
 * FOODTECHKOREA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.project.utils.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jooq.JSON;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Objects;

/**
 * The type Json utils.
 *
 * @author Foodtech Korea, bhkwon@foodtechkorea.com
 * @since 2017. 7. 18.
 */
public class JsonUtils {

    private final Gson gson;

    private final ObjectMapper mapper;

    private JsonUtils() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .setLenient()
                .create();
        mapper = new ObjectMapper();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    private static JsonUtils getInstance() {
        return new JsonUtils();
    }

    public static Gson getGson() {
        return getInstance().gson;
    }
    public static ObjectMapper getObjectMapper() {
        return getInstance().mapper;
    }

    /**
     * To json string.
     *
     * @param object the object
     * @return the string
     */
    public static String toJson(Object object) {
        return StringEscapeUtils.unescapeJava(getGson().toJson(object));
    }

    /**
     * From json t.
     *
     * @param <T>     the type parameter
     * @param jsonStr the json str
     * @param cls     the cls
     * @return the t
     */
    public static <T> T fromJson(String jsonStr, Class<T> cls) {
        return getGson().fromJson(jsonStr, cls);
    }

    /**
     * From json json element.
     *
     * @param json the json
     * @return the json element
     * @throws Exception the exception
     */
    public static JsonElement fromJson(String json){
        return getGson().toJsonTree(json);
    }

    /**
     * From json t.
     *
     * @param <T>     the type parameter
     * @param jsonStr the json str
     * @param type    the type
     * @return the t
     */
    public static <T extends Collection> T fromJson(String jsonStr, Type type) {
        try {
            return getGson().fromJson(jsonStr, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * To pretty json string.
     *
     * @param json the json
     * @return the string
     */
    public static String toPrettyJson(String json) {
        return StringEscapeUtils.unescapeJava(getGson().toJson(fromJson(json, Object.class)));
    }

    /**
     * To ObjectMapper pretty json string
     * @param object
     * @return
     */
    public static String toMapperJson(Object object){
        try {
            return getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * To ObjectMapper pretty json string
     * @param object
     * @return
     */
    public static String toMapperPrettyJson(Object object){
        try {
            return getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * To ObjectMapper Mapping json string
     * @param obj JSON String
     * @param clz 변환할 오브젝트 클래스
     * @param <T>
     * @return
     */
    public static<T> T toMapperObject(String obj, Class<T> clz) {
        if(StringUtils.isEmpty(obj)) return null;
        try {
            return getObjectMapper().readValue(obj, clz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * To ObjectMapper Mapping json string
     * @param obj JSON String
     * @param type 변환할 오브젝트 클래스
     * @param <T>
     * List 와 같은 형태는 TypeReference 형으로 처리
     * new TypeReference<List<Object>() {}
     * @return
     */
    public static<T> T toMapperObject(String obj, TypeReference<T> type) {
        if(StringUtils.isEmpty(obj)) return null;
        try {
            return getObjectMapper().readValue(obj, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * To ObjectMapper Mapping json string
     * @param json JSON String
     * @param clz 변환할 오브젝트 클래스
     * @param <T>
     * @return
     */
    public static<T> T jsonToObject(JSON json, Class<T> clz) {
        if(Objects.isNull(json)) return null;
        return toMapperObject(json.data(), clz);
    }

    /**
     * To ObjectMapper Mapping json string
     * @param json JSON String
     * @param type 변환할 오브젝트 클래스
     * @param <T>
     * List 와 같은 형태는 TypeReference 형으로 처리
     * new TypeReference<List<Object>() {}
     * @return
     */
    public static<T> T jsonToObject(JSON json, TypeReference<T> type) {
        if(Objects.isNull(json)) return null;
        return toMapperObject(json.data(), type);
    }

}