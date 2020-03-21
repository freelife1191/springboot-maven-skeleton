package com.project.exception.common.constant;

import com.project.common.constant.ResCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.event.Level;

import java.util.HashMap;
import java.util.Map;

import static com.project.common.constant.ResCode.*;

/**
 * Exception 타입에 따른 메세지 처리 상수
 * Created by KMS on 06/03/2020.
 */
@Getter
@AllArgsConstructor
public enum CommonError {
    /** RequestBody(JSON 데이터)가 없을 때 **/
    HttpMessageNotReadableException(ERR_PARAMS, "처리할 RequestBody(JSON 데이터)가 없습니다", false,  Level.ERROR),
    /** 데이터 등록 실패 에러핸들러 **/
    DataRegistrationFailedException(DATA_NOT_FOUND, DATA_REGISTRATION_FAIL.getMessage()+": 데이터 등록이 실패했습니다: ",true, Level.ERROR),
    /** 임시 파일 적용 시 BODY가 없을 때 **/
    DataNotFoundException(DATA_NOT_FOUND, "Data not found: 요청하신 데이터를 찾을 수 없습니다: ", true, Level.ERROR),

    /** 프로세스를 더이상 진행할 수 없을 때 **/
    UnprocessableException(ERROR, "", true, Level.ERROR),
    /** 파일이 존재하지 않을 때 **/
    FileNotExistException(FILE_NOT_EXIST, "", true, Level.ERROR),
    /** 파일 등록 필수 파라메터 체크 **/
    FileRequestFileNotException(ERR_PARAMS, "", true, Level.ERROR),
    /** 파일 덮어쓰기 여부 확인 **/
    FileDuplicateException(ERR_PARAMS, "", true, Level.ERROR),
    /** 파일 등록 필수 파라메터 체크 **/
    FileRequestParamRequiredException(ERR_PARAMS, "", true, Level.ERROR),
    /** 중복된 데이터 처리 에러 핸들러 **/
    DuplicateDataException(DATA_DUPLICATE, DATA_DUPLICATE.getMessage()+": ", true, Level.ERROR),
    /** 지원되지 않는 HttpMethod 에러 핸들러 **/
    HttpRequestMethodNotSupportedException(ERR_PARAMS, "지원되지 않는 HttpMethod: ", true, Level.ERROR),
    /** 데이터베이스 중복 키 에러 **/
    DuplicateKeyException(DATA_DUPLICATE, "중복된 키가 있어 데이터베이스 등록에 실패 했습니다: ", true, Level.ERROR),

    /** 파일이 용량 제한 에러 **/
    MaxUploadSizeExceededException(FILE_MAX_UPLOAD_SIZE_EXCEEDED, "첨부파일 크기가 제한값을 초과할 수 없습니다: ", false, Level.WARN),
    /** 파일이 용량 제한 에러 **/
    SizeLimitExceededException(FILE_MAX_UPLOAD_SIZE_EXCEEDED, "첨부파일 크기는 제한값을 초과할 수 없습니다: ", false, Level.WARN),

    /** RestTemplate API 통신 에러 핸들러 **/
    ResourceAccessException(ERROR, "API 통신이 원할하지 않습니다", false, Level.ERROR),
    /** RestTemplate API 통신 에러 핸들러 **/
    RestClientException(ERROR, "API 통신이 원할하지 않습니다", false, Level.ERROR),

    /** 사용자 정의 필수 파라메터 누락 에러 핸들러 **/
    RequestParamRequiredException(ERR_PARAMS, "", true, Level.WARN),
    /** 파라메터 유효성 체크 실패 에러 핸들러 **/
    ParameterValidationFailedException(ERR_PARAMS, "", true, Level.WARN),
    /** Parameter Validation Error **/
    MissingServletRequestParameterException(ERR_PARAMS, "", true, Level.ERROR),

    /** Parameter Validation Error **/
    BindException(ERR_PARAMS, "", true, Level.ERROR),
    /** Parameter Validation Error **/
    MethodArgumentNotValidException(ERR_PARAMS, "", true, Level.ERROR),
    /** JWT 토큰 복호화 ERROR 핸들러 **/
    InvalidSignatureException(ERR_PARAMS, "JWT 토큰 복호화 ERROR: ", true, Level.ERROR),

    /** AES 복호화 에러 **/
    AesDecryptException(ERROR, "AES 복호화 ERROR: ", true, Level.ERROR),
    /** AES 암호화 에러 **/
    AesEncryptException(ERROR, "AES 암호화 ERROR: ", true, Level.ERROR),

    /** 엑셀 데이터 처리중 에러 **/
    ExcelComponentException(ERROR, "엑셀 데이터 처리중 ERROR: ", true, Level.ERROR),

    /** 엑셀 업로드 시 캐치 하지 못한 그외 에러 **/
    ExcelReaderException(EXCEL_READER_ERROR, "엑셀 업로드 ERROR: ", true, Level.ERROR),
    /** 엑셀 업로드시 읽을 수 없는 엑셀 파일 에러 **/
    ExcelReaderFileException(EXCEL_READER_FILE_ERROR, "읽을 수 없는 엑셀 파일 입니다 (DRM 적용 또는 다른이유)", false, Level.ERROR),
    /** 엑셀 업로드 Field 에러 **/
    ExcelReaderFieldException(EXCEL_READER_FIELD_ERROR, "엑셀 업로드 FIELD ERROR :: 입력 데이터를 확인하세요", false, Level.WARN),
    ;

    private static Map<String, CommonError> nameToMap;

    /* 응답코드객체 */
    private final ResCode resCode;
    /* 응답메세지 */
    private final String message;
    /* e.getMessage true:추가, false:미추가 */
    private final boolean exception;
    /* Log Level */
    private final Level level;

    /**
     * Exception 으로 CommonError 맵으로 맵핑
     * @param e Exception 객체
     * @return CommonError 맵
     */
    public static CommonError getCommonError(Exception e) {
        if(nameToMap == null) {
            initMapping();
        }
        return nameToMap.get(e.getClass().getSimpleName());
    }

    /**
     * 맵 초기화
     */
    private static void initMapping() {
        nameToMap = new HashMap<>();
        for (CommonError commonError : values()) {
            nameToMap.put(commonError.name(), commonError);
        }
    }
}