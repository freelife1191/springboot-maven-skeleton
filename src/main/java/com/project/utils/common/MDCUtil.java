package com.project.utils.common;

import org.slf4j.MDC;
import org.slf4j.spi.MDCAdapter;

import java.util.Map;

/**
 * httpRequest가 존재하는 동안 데이터를 유지시키기 위한 유틸
 * http://logback.qos.ch/manual/mdc.html
 *
 * @since 2019.03.04
 */
public class MDCUtil {

    private static MDCAdapter mdc = MDC.getMDCAdapter();

    /**
     * The constant REQUEST_ID.
     */
    public static final String REQUEST_ID = "REQUEST_ID";

    /**
     * The constant REQUEST_SESSION_ID.
     */
    public static final String REQUEST_SESSION_ID = "REQUEST_SESSION_ID";

    /**
     * The constant HEADER_MAP_MDC.
     */
    public static final String HEADER_MAP_MDC = "HEADER_MAP_MDC";

    /**
     * The constant PARAMETER_MAP_MDC.
     */
    public static final String PARAMETER_MAP_MDC = "PARAMETER_MAP_MDC";

    /**
     * The constant USER_INFO_MDC.
     */
    public static final String USER_INFO_MDC = "USER_INFO_MDC";

    /**
     * The constant REQUEST_URI_MDC.
     */
    public static final String REQUEST_URI_MDC = "REQUEST_URI_MDC";

    /**
     * The constant REQUEST_METHOD_TYPE.
     */
    public static final String REQUEST_METHOD_TYPE = "REQUEST_METHOD_TYPE";

    /**
     * The constant AGENT_DETAIL_MDC.
     */
    public static final String AGENT_DETAIL_MDC = "AGENT_DETAIL_MDC";

    /**
     * The constant BEFORE_REQUEST_MESSAGE.
     */
    public static final String BEFORE_REQUEST_MESSAGE = "BEFORE_REQUEST_MESSAGE";

    /**
     * Set.
     *
     * @param key   the key
     * @param value the value
     */
    public static void set(String key, String value) {
        mdc.put(key, value);
    }

    /**
     * Sets json value.
     *
     * @param key   the key
     * @param value the value
     */
    public static void setJsonValue(String key, Object value) {
        if (value != null) {
            String json = JsonUtils.toJson(value);
            mdc.put(key, json);
        }
    }

    /**
     * Get string.
     *
     * @param key the key
     * @return the string
     */
    public static String get(String key) {
        return mdc.get(key);
    }

    /**
     * Clear.
     */
    public static void clear() {
        MDC.clear();
    }

    /**
     * Sets error attribute.
     *
     * @param errorAttribute the error attribute
     */
    public static void setErrorAttribute(Map<String, Object> errorAttribute) {
        if (errorAttribute.containsKey("path")) {
            set(REQUEST_URI_MDC, (String) errorAttribute.get("path"));
        }
    }
}
