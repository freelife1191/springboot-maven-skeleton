package com.project.common.Validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.Validation;
import java.util.Collection;

/**
 * Collection 타입에 대한 Validation 체크
 * 참조1: https://github.com/HomoEfficio/dev-tips/wiki/SpringMVC%EC%97%90%EC%84%9C-Collection%EC%9D%98-Validation
 * 참조2: https://gompangs.tistory.com/entry/Spring-Valid-Collection-Validation-%EA%B4%80%EB%A0%A8
 *
 * 사용예시
 * private final CustomCollectionValidator customCollectionValidator;
 *
 * // Collection 의 경우 @Valid 로 유효성 검증이 되지 않아 직접 validate
 * customCollectionValidator.validate(idList, bindingResult);
 *
 * // Collection 유효성 검증 실패에 대한 예외처리
 * if (bindingResult.hasErrors())
 *    throw new BindException(bindingResult);
 *
 * Created by KMS on 14/08/2020.
 */
@Component
public class CustomCollectionValidator implements Validator {

    private SpringValidatorAdapter validator;

    public CustomCollectionValidator() {
        this.validator = new SpringValidatorAdapter(
                Validation.buildDefaultValidatorFactory().getValidator()
        );
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(target instanceof Collection){
            Collection collection = (Collection) target;

            for (Object object : collection) {
                validator.validate(object, errors);
            }
        } else {
            validator.validate(target, errors);
        }

    }
}
