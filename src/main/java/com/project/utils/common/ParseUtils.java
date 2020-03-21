package com.project.utils.common;

import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;

/**
 * Parse Utils
 * Created by KMS on 22/02/2020.
 */
public class ParseUtils {

    /**
     * 모든 Object의 String 값을 가져온다.
     *
     * <pre>
     *  null =&gt; null;
     *  String =&gt; string.trim();
     *  Collection =&gt; delimited by &quot;;&quot; string
     *  Object -&gt; obj.toString();
     * </pre>
     */
    public static String getSafeString(Object obj) {
        return getSafeString(obj, null);
    }

    @SuppressWarnings("rawtypes")
    public static String getSafeString(Object obj, String def) {
        if (obj == null) {
            return def;
        } else if (obj instanceof String) {
            return String.valueOf(obj).trim();
        } else if (obj instanceof Collections) {
            return StringUtils.collectionToDelimitedString((Collection) obj,";");
        } else {
            return obj.toString();
        }
    }

    /**
     *
     * 문자를 int 형으로 변환 <br>
     * Exception 발생시 0 을 반환
     */
    public static int parseInt(Object obj) {
        return parseInt(obj, 0);
    }

    /**
     * 문자를 int 형으로 변환 <br>
     * Exception 발생시 default_num 을 반환
     */
    public static int parseInt(Object obj, int default_num) {
        int parseInt;
        try {
            if(obj instanceof Integer) {
                parseInt =  (Integer) obj;
            } else {
                String str = getSafeString( obj);
                if (str != null) {
                    parseInt = Integer.parseInt(str.trim());
                } else {
                    parseInt = default_num;
                }
            }
        } catch (Exception nf) {
            parseInt = default_num;
        }
        return parseInt;
    }

}
