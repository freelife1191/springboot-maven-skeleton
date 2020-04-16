package com.project.component.code.dto;

import com.project.component.code.domain.CommonCode;
import com.project.component.code.domain.CommonDetailCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * 공통 코드 DTO
 * Created by KMS on 30/03/2020.
 */
@Getter
@Setter
@ToString
public class CommonCodeDto {
    /* 공통 메인 코드 */
    private CommonCode code;
    /* 공통 상세 코드 */
    private CommonDetailCode detailCode;

    public CommonCodeDto() {
        this.code = new CommonCode();
        this.detailCode = new CommonDetailCode();
    }

    @Builder
    public CommonCodeDto(CommonCode code, CommonDetailCode detailCode) {
        this.code = Objects.isNull(code) ? new CommonCode() : code;
        this.detailCode = Objects.isNull(detailCode) ? new CommonDetailCode() : detailCode;
    }
}
