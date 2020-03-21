package com.project.common.annotation;

import java.lang.annotation.*;

/**
 * QueryString -> 객체 맵핑 어노테이션
 * Controller의 파라메터 받는 곳에 객체로 받도록 지정 Object object
 * 객체의 변수에 @ParamName("바인딩값") 형식으로 사용
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamName {

    /**
     * The name of the request parameter to bind to.
     */
    String value();

}
