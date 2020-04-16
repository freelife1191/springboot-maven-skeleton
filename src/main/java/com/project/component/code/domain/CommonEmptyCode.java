package com.project.component.code.domain;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 공통 누락 코드
 * Created by KMS on 30/03/2020.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommonEmptyCode {
    /* 등록일시 */
    private LocalDateTime createdAt;
    /* 그룹코드 */
    private String groupCode;
    /* 공통 메인 코드 ID */
    private Integer commonCodeId;
    /* 공통상세코드 */
    private Integer detailCode;
    /* 누락 값 */
    private String value;

    @Builder
    public CommonEmptyCode(LocalDateTime createdAt, String groupCode, Integer commonCodeId, Integer detailCode, String value) {
        this.createdAt = createdAt;
        this.groupCode = groupCode;
        this.commonCodeId = commonCodeId;
        this.detailCode = detailCode;
        this.value = value;
    }
}
