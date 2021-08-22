package com.project.exception.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.exception.common.constant.CommonError;
import com.project.utils.common.AuthUtils;
import com.project.utils.common.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by KMS on 06/11/2019.
 */
@Slf4j
public class ErrorUtils {

    /**
     * Exception Handler ErrorMap 기본 셋팅
     * @param request
     * @throws IOException
     */
    public static Map<String, Object> setErrorMap(HttpServletRequest request, WebRequest webRequest) throws Exception {
        return setErrorMap(request, webRequest, new LinkedHashMap<>(), null);
    }

    /**
     * Exception Handler ErrorMap 기본 셋팅
     * @param request
     * @throws IOException
     */
    public static Map<String, Object> setErrorMap(HttpServletRequest request, WebRequest webRequest, String secretKey) throws Exception {
        return setErrorMap(request, webRequest, new LinkedHashMap<>(), secretKey);
    }

    /**
     * Exception Handler ErrorMap 기본 셋팅
     * @param request
     * @throws IOException
     */
    public static Map<String, Object> setErrorMap(HttpServletRequest request, WebRequest webRequest, Map<String, Object> errorMap, String secretKey) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        errorMap = getErrorMap(errorMap, request); //에러 맵 생성
        if(StringUtils.isNotEmpty(secretKey)){
            try {
                String userId = AuthUtils.getUserId(request.getHeader("jwt"), secretKey);
                errorMap.put("Request USER ID", userId);
            } catch (Exception e) {
                log.error("JWT 복호화 해제 실패: ERROR_MSG = {}, JWT = {}",e.getMessage(),request.getHeader("jwt"));
            }
        }
        Object body = webRequest.getAttribute("body", RequestAttributes.SCOPE_REQUEST);
        if(body instanceof String)
            body = objectMapper.readValue((String) body, Map.class);
        errorMap.put("Request Body",body);
        return errorMap;
    }

    /**
     * Error Map 생성
     * 에러 메세지 맵을 생성하여 메신저에 전송한다
     * @param errorMap
     * @param request
     * @return
     */
    public static Map<String, Object> getErrorMap(Map<String, Object> errorMap, HttpServletRequest request) {

        if(errorMap == null) errorMap = new LinkedHashMap<>();

        if(request.getHeader("jwt") != null)
            errorMap.put("Request JWT",request.getHeader("jwt"));
        errorMap.put("Request URI",request.getRequestURI());
        errorMap.put("HttpMethod",request.getMethod());
        errorMap.put("Servlet Path",request.getServletPath());
        errorMap.put("Client IP",request.getLocalAddr());
        errorMap.put("QueryString",request.getQueryString());
        errorMap.put("Parameters",request.getParameterMap());
        return errorMap;
    }

    /**
     * Level 별 에러메세지 출력
     * @param error
     * @param CLASS_NAME
     * @param ERROR_MSG
     * @param errorMap
     * @param e
     */
    public static void logWriter(CommonError error, String CLASS_NAME, String ERROR_MSG, Map<String, Object> errorMap, Exception e) {
        logWriter(error, CLASS_NAME, ERROR_MSG, errorMap, e, null);
    }

    /**
     * Level 별 에러메세지 출력
     * @param error
     * @param CLASS_NAME
     * @param ERROR_MSG
     * @param errorMap
     * @param e
     */
    public static void logWriter(CommonError error, String CLASS_NAME, String ERROR_MSG, Map<String, Object> errorMap, Exception e, String CUSTOM_MSG) {

        String EX_MSG = StringUtils.isEmpty(CUSTOM_MSG) ? ExceptionUtils.getMessage(e) : CUSTOM_MSG + "\n" + ExceptionUtils.getMessage(e);
        //에러메세지 출력
        switch (error.getLevel()){
            case INFO:
                ErrorUtils.infoWriter(CLASS_NAME, ERROR_MSG, errorMap, EX_MSG);
                break;
            case WARN:
                ErrorUtils.warnWriter(CLASS_NAME, ERROR_MSG, errorMap, EX_MSG);
                break;
            case ERROR:
                ErrorUtils.errorWriter(CLASS_NAME, ERROR_MSG, errorMap, EX_MSG);
                break;
        }
    }

    /**
     * 에러메세지 출력
     * @param CLASS_NAME
     * @param ERROR_MSG
     * @param errorMap
     * @param e
     */
    public static void errorWriter(String CLASS_NAME, String ERROR_MSG, Map<String, Object> errorMap, Exception e){
        log.error("[{}] ERROR 메세지 :: {}\n\n요청 정보 ::\n{}\n\nException ::\n{}",CLASS_NAME,ERROR_MSG, JsonUtils.toJson(errorMap),e.getMessage(),e);
    }

    /**
     * 에러메세지 출력
     * @param CLASS_NAME
     * @param ERROR_MSG
     * @param errorMap
     * @param errorMsg
     */
    public static void errorWriter(String CLASS_NAME, String ERROR_MSG, Map<String, Object> errorMap, String errorMsg){
        log.error("[{}] ERROR 메세지 :: {}\n\n요청 정보 ::\n{}\n\nException ::\n{}",CLASS_NAME,ERROR_MSG, JsonUtils.toJson(errorMap), errorMsg);
    }

    /**
     * WARN 에러메세지 출력
     * @param CLASS_NAME
     * @param ERROR_MSG
     * @param errorMap
     * @param errorMsg
     */
    public static void warnWriter(String CLASS_NAME, String ERROR_MSG, Map<String, Object> errorMap, String errorMsg){
        log.warn("[{}] WARN 메세지 :: {}\n\n요청 정보 ::\n{}\n\nException ::\n{}",CLASS_NAME,ERROR_MSG, JsonUtils.toJson(errorMap),errorMsg);
    }

    /**
     * INFO 에러메세지 출력
     * @param CLASS_NAME
     * @param ERROR_MSG
     * @param errorMap
     * @param errorMsg
     */
    public static void infoWriter(String CLASS_NAME, String ERROR_MSG, Map<String, Object> errorMap, String errorMsg){
        log.info("[{}] INFO 메세지 :: {}\n\n요청 정보 ::\n{}\n\nException ::\n{}",CLASS_NAME,ERROR_MSG, JsonUtils.toJson(errorMap),errorMsg);
    }
}