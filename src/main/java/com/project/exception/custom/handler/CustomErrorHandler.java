package com.project.exception.custom.handler;

/**
 * Created by KMS on 29/09/2019.
 * 커스텀 Controller Exception Handler
 */

import com.project.api.sample.controller.SampleApiController;
import com.project.common.domain.CommonResult;
import com.project.exception.custom.CustomException;
import com.project.exception.utils.ErrorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.project.common.constant.ResCode.ERROR;

@Slf4j
@RestControllerAdvice(assignableTypes = SampleApiController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomErrorHandler {

    private String CLASS_NAME = "ApiController";

    private Map<String, Object> errorMap = new LinkedHashMap<>();

    /**
     * CustomException 에러 핸들러 예시
     * @param e
     * @param request
     * @param webRequest
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public CommonResult customException(Exception e, HttpServletRequest request, WebRequest webRequest) throws Exception {
        String ERROR_MSG = e.getMessage();

        errorMap = ErrorUtils.setErrorMap(request, webRequest); // ErrorMap 기본 셋팅

        ErrorUtils.errorWriter(CLASS_NAME, ERROR_MSG, errorMap, e.getMessage()); //에러메세지 출력
        return new CommonResult<>(ERROR, ERROR_MSG, errorMap);
    }

}
