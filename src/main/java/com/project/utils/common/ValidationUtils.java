package com.project.utils.common;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Objects;
import java.util.Set;

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
}
