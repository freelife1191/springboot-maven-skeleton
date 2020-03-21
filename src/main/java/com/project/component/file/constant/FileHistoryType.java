package com.project.component.file.constant;

/**
 * Created by KMS on 24/09/2019.
 *
 * 파일 히스토리 이력 타입
 */
public enum  FileHistoryType {
    ALL, //전체 조회조건
    TEMP_UPLOAD, //임시파일 업로드
    TEMP_APPLY, //임시파일 적용처리
    UPLOAD, //업로드
    DELETE, //삭제
    UPDATE,  //업데이트
    MIGRATION //마이그레이션
}