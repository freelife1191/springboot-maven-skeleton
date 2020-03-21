package com.project.exception.common.handler;

/**
 * Created by KMS on 29/09/2019.
 * 공통 Controller Exception Handler
 */

import com.project.common.domain.CommonResult;
import com.project.component.excel.service.ExcelReader;
import com.project.exception.common.*;
import com.project.exception.common.constant.CommonError;
import com.project.exception.excel.ExcelComponentException;
import com.project.exception.excel.ExcelReaderException;
import com.project.exception.excel.ExcelReaderFieldException;
import com.project.exception.excel.ExcelReaderFileException;
import com.project.exception.file.FileDuplicateException;
import com.project.exception.file.FileNotExistException;
import com.project.exception.file.FileRequestFileNotException;
import com.project.exception.file.FileRequestParamRequiredException;
import com.project.utils.common.ErrorUtils;
import com.project.utils.common.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.jwt.crypto.sign.InvalidSignatureException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.project.common.constant.ResCode.ERROR;

@Slf4j
@RestControllerAdvice
public class CommonErrorHandler {

    private String CLASS_NAME = "Common";

    private Map<String, Object> errorMap = new LinkedHashMap<>();

    @Value("${properties.file.max-upload-size}")
    private long MAX_UPLOAD_SIZE;

    @Value("${jwt.secret-key}")
    private String secretKey;

    /**
     * 공통 에러 처리 핸들러 (에러메세지 처리)
     * 로그상에 ERROR 메세지로 기록되고 두레이 메신저 알림이 옴
     * @param e
     * @param request
     * @param webRequest
     * @return
     * @throws Exception
     */
    @ExceptionHandler({
            //프로세스를 더이상 진행할 수 없을 때
            UnprocessableException.class,
            // 파일이 존재하지 않을 때
            FileNotExistException.class,
            // 파일 등록 파일 유무 체크
            FileRequestFileNotException.class,
            // 파일 덮어쓰기 여부 확인
            FileDuplicateException.class,
            // 파일 등록 필수 파라메터 체크
            FileRequestParamRequiredException.class,
            // 파라메터 유효성 체크 실패 에러 핸들러
            ParameterValidationFailedException.class,
            // 데이터 등록 실패 에러핸들러
            DataRegistrationFailedException.class,
            // 임시 파일 적용 시 BODY가 없을 때
            DataNotFoundException.class,
            // RequestBody(JSON 데이터)가 없을 때
            HttpMessageNotReadableException.class,
            // 중복된 데이터 처리 에러 핸들러
            DuplicateDataException.class,
            // 지원되지 않는 HttpMethod 에러 핸들러
            HttpRequestMethodNotSupportedException.class,
            // 사용자 정의 필수 파라메터 누락 에러 핸들러
            RequestParamRequiredException.class,
            // Parameter Validation Error
            MissingServletRequestParameterException.class,
            // RestTemplate API 통신 에러 핸들러
            RestClientException.class,
            // RestTemplate API 통신 에러 핸들러
            ResourceAccessException.class,
            // JWT 토큰 복호화 ERROR 핸들러
            InvalidSignatureException.class,
            // 데이터베이스 중복 키 에러
            DuplicateKeyException.class,
            // 최대 파일 용량 초과 에러
            MaxUploadSizeExceededException.class,
            // 파일이 용량 제한 에러
            SizeLimitExceededException.class,
            // Parameter Validation Error
            BindException.class,
            // Parameter Validation Error
            MethodArgumentNotValidException.class,
            // AES 복호화 에러
            AesDecryptException.class,
            // AES 암호화 에러
            AesEncryptException.class,
            // 엑셀 데이터 처리중 에러
            ExcelComponentException.class,
            // 엑셀 업로드 필드 에러 핸들러
            ExcelReaderFieldException.class,
            // 엑셀 업로드 시 캐치 하지 못한 그외 에러
            ExcelReaderException.class,
            // 엑셀 업로드시 읽을 수 없는 엑셀 파일 에러
            ExcelReaderFileException.class,
    })
    public CommonResult<?> CommonErrorException(Exception e, HttpServletRequest request, WebRequest webRequest) throws Exception {

        // 공통에러상수 객체 셋팅
        CommonError error = CommonError.getCommonError(e);

        String ERROR_MSG = error.isException() && StringUtils.isNotEmpty(e.getMessage()) ? error.getMessage()+ExceptionUtils.getRootCauseMessage(e) : error.getMessage();

        errorMap = ErrorUtils.setErrorMap(request, webRequest, secretKey); // 에러 맵 기본 셋팅
        CommonResult<?> commonResult = new CommonResult<>(error.getResCode(), ERROR_MSG, errorMap);

        /* Exception 별 커스텀 처리 */
        switch (error) {
            case MaxUploadSizeExceededException: // 최대 파일 용량 초과 에러
                ERROR_MSG += "제한값 ( " + MAX_UPLOAD_SIZE + " MB )";
                break;
            case BindException: // Parameter Validation Error
                ERROR_MSG = getBindResultFieldErrorMessage(((BindException) e).getBindingResult());
                break;
            case MethodArgumentNotValidException: // Parameter Validation Error
                ERROR_MSG = getBindResultFieldErrorMessage(((MethodArgumentNotValidException) e).getBindingResult());
                break;
            case ExcelReaderFieldException: // 엑셀 업로드 필드 에러 핸들러
                commonResult = new CommonResult<>(error.getResCode(), ERROR_MSG, ExcelReader.errorFieldList);
                errorMap.put("CommonResult", commonResult);
                break;
        }

        // Level 별 에러메시지 출력
        ErrorUtils.logWriter(error, CLASS_NAME, ERROR_MSG, errorMap, e);
        return commonResult;
    }

    /**
     * Handler 에서 예외처리 되지 않은 Exception 처리
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public CommonResult<Map<String, Object>> allException(Exception e, HttpServletRequest request, WebRequest webRequest) throws Exception {
        String ERROR_MSG = e.getMessage();

        errorMap = ErrorUtils.setErrorMap(request, webRequest, secretKey); // 에러 맵 기본 셋팅

        ErrorUtils.errorWriter(CLASS_NAME, ERROR_MSG, errorMap, e); //에러메세지 출력
        return new CommonResult<>(ERROR, ERROR_MSG, errorMap);
    }

    /**
     * BindException Field 메세지 가공
     * @param bindingResult
     * @return
     */
    protected String getBindResultFieldErrorMessage(BindingResult bindingResult) {

        Map<String, Object> resultMap = new LinkedHashMap<>();

        resultMap.put("title","Parameter Validation Error");
        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
        List<Map<String, Object>> paramList = new ArrayList<>();
        for (FieldError fieldError: fieldErrorList){

            Map<String, Object> resultParam = new LinkedHashMap<>();
            resultParam.put(fieldError.getField(),fieldError.getRejectedValue());
            resultParam.put("message",fieldError.getDefaultMessage());
            paramList.add(resultParam);
            /*
            log.debug("## ERROR getField = {}", fieldError.getField());
            log.debug("## ERROR getRejectedValue = {}", fieldError.getRejectedValue());
            log.debug("## ERROR getArguments = {}", fieldError.getArguments());
            log.debug("## ERROR getCode = {}", fieldError.getCode());
            log.debug("## ERROR getCodes = {}", fieldError.getCodes());
            log.debug("## ERROR getObjectName = {}", fieldError.getObjectName());
            log.debug("## ERROR getDefaultMessage = {}", fieldError.getDefaultMessage());
            */
        }
        resultMap.put("fields",paramList);

        return JsonUtils.toMapperPrettyJson(resultMap);
    }

}