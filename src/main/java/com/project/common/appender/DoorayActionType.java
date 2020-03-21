package com.project.common.appender;

/**
 * 두레이 Action Type 정의
 *
 * Created by KMS on 25/10/2019.
 */
public enum DoorayActionType {

    BUTTON("button"),
    SELECT("select");

    private String code;

    private DoorayActionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
