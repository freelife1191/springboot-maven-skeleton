package com.project.common.domain;


import com.project.common.constant.ResCode;
import lombok.*;

/**
 * REST API 응답 공통 객체
 * @param <T> 결과 객체
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommonResult<T> {

    private int code;
    private String msg;
    private T result;

    public CommonResult(ResCode code, String msg, T result) {
        this.code = code.getCode();
        this.msg = msg;
        this.result = result;
    }
}
