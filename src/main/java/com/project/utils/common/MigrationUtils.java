package com.project.utils.common;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TIS 마이그레이션 유틸
 * Created by KMS on 22/02/2020.
 */
public class MigrationUtils {

    /**
     * 꺽쇠패턴 앞문자 가져오기
     * @param target
     * @return
     */
    public static String getPrifixStr(String target) {

        if(StringUtils.isEmpty(target)) return null;

        Pattern regex = Pattern.compile("^[가-힣]+");
        Matcher matcher = regex.matcher(target);

        if(matcher.find())
            target = matcher.group();

        return target;

    }

    /**
     * 꺽쇠패턴 뒷문자 가져오기
     * @param target
     * @return
     */
    public static String getSuffixStr(String target) {

        if(StringUtils.isEmpty(target)) return null;

        Pattern regex = Pattern.compile("(?![가-힣]+>)[가-힣]+");
        Matcher matcher = regex.matcher(target);

        if(matcher.find())
            target = matcher.group();

        return target;
    }

    /**
     * 위경도 값 BigDecimal 로 변환
     * 마이너스 값은 양수로 변환하여 리턴
     * @param decimalStr
     * @return
     */
    public static BigDecimal getBigDecimal(String decimalStr) {
        if(StringUtils.isEmpty(decimalStr)) return null;
        return getBigDecimal(new BigDecimal(decimalStr).setScale(14, RoundingMode.DOWN));
    }

    /**
     * 위경도 값 BigDecimal 로 변환
     * 마이너스 값은 양수로 변환하여 리턴
     * @param decimal
     * @return
     */
    public static BigDecimal getBigDecimal(BigDecimal decimal) {
        return decimal.compareTo(BigDecimal.ZERO) < 0 ? decimal.multiply(new BigDecimal(-1)) : decimal;
    }

    /**
     * Integer, String 데이터 Boolean 변환 유틸
     * false 와 null 데이터는 null을 리턴한다
     * @param obj
     * @return
     */
    public static Boolean booleanIfFalseNull(Object obj) {
        Boolean isTrue = booleanIfNull(obj);
        if(Objects.isNull(isTrue) || !isTrue) return null;
        return true;
    }

    /**
     * Integer, String 데이터 Boolean 변환 유틸
     * null 데이터는 null을 리턴한다
     * @param obj
     * @return
     */
    public static Boolean booleanIfNull(Object obj) {
        Boolean bool = null;

        if(Objects.nonNull(obj)){
            if( obj instanceof Integer)
                bool = BooleanUtils.toBoolean((Integer) obj);
            else bool = BooleanUtils.toBoolean((String) obj);
        }
        return bool;
    }

    /**
     * Timestamp 데이터를 LocalDate로 변환한다
     * @param object
     * @return
     */
    public static LocalDate timestampToLocalDate(Long object) {
        if(Objects.isNull(object)) return null;
        return new Timestamp(object).toLocalDateTime().toLocalDate();
    }
}
