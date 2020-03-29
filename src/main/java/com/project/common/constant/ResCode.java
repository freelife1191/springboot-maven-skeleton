package com.project.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
public enum ResCode {

    SUCCESS(100, "SUCCESS","SUCCESS"),

    ERR_PARAMS(200, "ERR PARAMS","Error Params"),
    RESULT_NULL(201, "RESULT NULL","Result Null"),
    DATA_DUPLICATE(202, "DATA DUPLICATE", "Data Duplicate"),
    DATA_NOT_FOUND(203, "DATA NOT FOUND", "Data not found"),
    EXCEED_MAXIMUM_DATA(204, "EXCEED_MAXIMUM_DATA", "Exceed Maximum Data"),
    DATA_REGISTRATION_FAIL(205, "DATA_REGISTRATION_FAIL", "Data Registration Failed"),

    ERROR(500, "ERROR", "ERROR"),
    FAIL(900, "FAIL","FAIL"),

    /* File Error Message */
    FILE_REQUEST_MISSING_PART(3000, "FILE REQUEST MISSING PART ERROR", "File Request Missing Part Error"),
    FILE_DUPLICATE(3001, "FILE DUPLACATE", "File Duplacate"),
    FILE_MAX_UPLOAD_SIZE_EXCEEDED(3002, "FILE MAX UPLOAD SIZE EXCEEDED","File Max Upload Size Exceeded"),

    FILE_REQUEST_ERROR(3010, "FILE REQUEST ERROR", "File Request Error"),
    FILE_NOT_EXIST(3101, "FILE NOT EXIST", "File Not Exist"),

    EXCEL_ERROR(4000, "EXCEL ERROR", "Excel Error"),
    // 엑셀 업로드 시 캐치 하지 못한 그외 에러
    EXCEL_READER_ERROR(4001, "EXCEL READER ERROR", "Excel Reader Error"),
    // 엑셀 업로드시 읽을 수 없는 엑셀 파일 에러
    EXCEL_READER_FILE_ERROR(4010, "EXCEL READER FILE ERROR", "Excel Reader File Error"),
    // 엑셀 업로드 Field 에러
    EXCEL_READER_FIELD_ERROR(4011, "EXCEL READER FIELD ERROR", "Excel Reader Field Error"),
    ;

    private static Map<Integer, ResCode> codeToMap;

    private final int code;
    private final String label;
    private final String message;

    /**
     * 코드 값으로 ResCode 맵으로 맵핑
     * @param code
     * @return
     */
    public static ResCode getResCode(int code) {
        if(codeToMap == null) {
            initMapping();
        }
        return codeToMap.get(code);
    }

    /**
     * 맵 초기화
     */
    private static void initMapping() {
        codeToMap = new HashMap<>();
        for (ResCode resCode : values()) {
            codeToMap.put(resCode.code, resCode);
        }
    }
}
