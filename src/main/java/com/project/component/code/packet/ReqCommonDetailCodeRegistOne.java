package com.project.component.code.packet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * Created by KMS on 30/03/2020.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel("공통 상세 코드 단건 등록 객체")
public class ReqCommonDetailCodeRegistOne extends ReqCommonDetailCodeRegist {
    @NotNull
    @ApiModelProperty(value = "공통코드 ID", required = true)
    private Integer commonCodeId;

    @Builder
    public ReqCommonDetailCodeRegistOne(String detailCodeNm, String detailCodeEngNm, String detailCodeDc, Integer order, String etc1, String etc2, String etc3, String etc4, String etc5, Integer commonCodeId) {
        super(detailCodeNm, detailCodeEngNm, detailCodeDc, order, etc1, etc2, etc3, etc4, etc5);
        this.commonCodeId = commonCodeId;
    }
}