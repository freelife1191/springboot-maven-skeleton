package com.project.component.excel.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 엑셀 업로드 Field 에러 상수
 * Created by KMS on 18/03/2020.
 */
@Getter
@AllArgsConstructor
public enum ExcelReaderFieldError {
    TYPE("잘못된 데이터 타입: "),
    EMPTY("필수 입력값 누락"),
    VALID("유효성 검증 실패"),
    UNKNOWN("알수 없는 에러")
    ;

    private static Map<String, ExcelReaderFieldError> messageToMap;

    /** 메세지 */
    private final String message;

    /**
     * 에러명으로 ExcelReaderErrorConstant 맵으로 맵핑
     * @param name
     * @return
     */
    public static ExcelReaderFieldError getExcelReaderErrorConstant(String name) {
        if(messageToMap == null) {
            initMapping();
        }
        return messageToMap.get(name);
    }

    /**
     * 맵 초기화
     */
    private static void initMapping() {
        messageToMap = new HashMap<>();
        for (ExcelReaderFieldError excelReaderFieldError : values()) {
            messageToMap.put(excelReaderFieldError.name(), excelReaderFieldError);
        }
    }

}
