package com.project.utils.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Request 정보를 상세히 볼수 있는 유틸
 * Created by KMS on 08/05/2020.
 */
public class InfoUtils {

    /**
     * InfoMap 기본 셋팅
     * @param request
     * @throws IOException
     */
    public static Map<String, Object> setInfoMap(HttpServletRequest request, WebRequest webRequest) throws Exception {
        return setInfoMap(request, webRequest, new LinkedHashMap<>(), null);
    }

    /**
     * InfoMap 기본 셋팅
     * @param request
     * @throws IOException
     */
    public static Map<String, Object> setInfoMap(HttpServletRequest request, WebRequest webRequest, String secretKey) throws Exception {
        return setInfoMap(request, webRequest, new LinkedHashMap<>(), secretKey);
    }

    /**
     * InfoMap 기본 셋팅
     * @param request
     * @throws IOException
     */
    public static Map<String, Object> setInfoMap(HttpServletRequest request, WebRequest webRequest, Map<String, Object> infoMap, String secretKey) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configOverride(FileDescriptor.class).setIsIgnoredType(true);
        // objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        infoMap = getRequestInfoMap(infoMap, request); //에러 맵 생성
        if(StringUtils.isNotEmpty(secretKey))
            infoMap.put("Request USER ID", AuthUtils.getUserId(request.getHeader("jwt"), secretKey));
        Object body = webRequest.getAttribute("body", RequestAttributes.SCOPE_REQUEST);
        // if(body instanceof String)
        //     body = objectMapper.readValue((String) body, Map.class);
        infoMap.put("Request Body", JsonUtils.toJson(body));
        return infoMap;
    }

    /**
     * Info Map 생성
     * 정보 메세지 맵을 생성
     * @param infoMap
     * @param request
     * @return
     */
    public static Map<String, Object> getRequestInfoMap(Map<String, Object> infoMap, HttpServletRequest request) {

        if(infoMap == null) infoMap = new LinkedHashMap<>();

        if(request.getHeader("jwt") != null)
            infoMap.put("Request JWT",request.getHeader("jwt"));
        infoMap.put("Request URI",request.getRequestURI());
        infoMap.put("HttpMethod",request.getMethod());
        infoMap.put("Servlet Path",request.getServletPath());
        infoMap.put("Client IP",request.getLocalAddr());
        infoMap.put("QueryString",request.getQueryString());
        infoMap.put("Parameters",request.getParameterMap());
        return infoMap;
    }

}
