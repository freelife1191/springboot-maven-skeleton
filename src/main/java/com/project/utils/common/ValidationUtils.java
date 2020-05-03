package com.project.utils.common;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 유효성 검증 유틸
 * Created by KMS on 24/04/2020.
 */
public class ValidationUtils {

    /* Validator 객체 */
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 객체에서 요청 필드명과 일치한 Validation 검증 데이터를 가져온다
     * 검증데이터로 해당 검증을 통과했는지 여부를 판단할 수 있다
     * @param object
     * @param fieldName
     * @param <T>
     * @return
     */
    public static<T> ConstraintViolation<T> getValidData(T object, String fieldName) {
        Set<ConstraintViolation<T>> constraintValidations = validator.validate(object);
        return constraintValidations.stream()
                .filter(data -> data.getPropertyPath().toString().equals(fieldName))
                .findFirst().orElse(null);
    }

    /**
     * 객체에 대한 Validation 을 검증해서
     * 검증을 통과하면 true 통과하지 못하면 false
     * @param object
     * @param <T>
     */
    public static<T> boolean isValid(T object, String fieldName) {
        ConstraintViolation<T> validData = getValidData(object, fieldName);
        return Objects.isNull(validData);
    }

    /**
     * 객체에 대한 Validation 을 검증해서
     * NotEmpty, NotNull 이 설정된 속성에 대한 검증을 통과하지 못하면 true 통과하면 false
     * @param object
     * @param <T>
     */
    public static<T> boolean isEmpty(T object, String fieldName) {
        return !isNotEmpty(object, fieldName);
    }

    /**
     * 객체에 대한 Validation 을 검증해서
     * NotEmpty, NotNull 이 설정된 속성에 대한 검증을 통과하면 true 통과하지 못하면 false
     * @param object
     * @param <T>
     */
    public static<T> boolean isNotEmpty(T object, String fieldName) {
        ConstraintViolation<T> validData = getValidData(object, fieldName);
        if(Objects.isNull(validData)) return true;
        return !validData.getMessageTemplate().contains("NotEmpty") && !validData.getMessageTemplate().contains("NotNull");
    }

    /**
     * IP 주소 형식 유효성 검증
     * @param data
     * @return
     */
    public static boolean isIp(String data) {
        if(StringUtils.isEmpty(data)) return false;
        Pattern pattern = Pattern.compile("(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])");
        Matcher matcher = pattern.matcher(data);
        return matcher.find();
    }

    /**
     * PORT 데이터 범위 65535 까지 아니면 false
     * @param data
     * @return
     */
    public static boolean isPort(Integer data) {
        if(Objects.isNull(data)) return false;
        return data <= 65535;
    }
}
