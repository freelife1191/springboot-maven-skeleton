package com.project.component.code.packet;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * Created by KMS on 27/03/2020.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReqCommonDetailCodeMod {
    @NotNull
    @ApiModelProperty(value = "공통코드 ID", required = true)
    private Integer commonCodeId;
    @NotNull
    @ApiModelProperty(value = "공통상세코드", required = true)
    private Integer detailCode;
    @ApiModelProperty("공통상세코드 명")
    private String detailCodeNm;
    @ApiModelProperty("공통상세코드 영문명")
    private String detailCodeEngNm;
    @ApiModelProperty("공통상세코드 설명")
    private String detailCodeDc;
    @ApiModelProperty("정렬순서")
    private Integer order;
    @ApiModelProperty("사용여부")
    private Boolean enabled;
    @ApiModelProperty("기타1")
    private String etc1;
    @ApiModelProperty("기타2")
    private String etc2;
    @ApiModelProperty("기타3")
    private String etc3;
    @ApiModelProperty("기타4")
    private String etc4;
    @ApiModelProperty("기타5")
    private String etc5;

    @Builder
    public ReqCommonDetailCodeMod(@NotNull Integer commonCodeId, @NotNull Integer detailCode, String detailCodeNm, String detailCodeEngNm, String detailCodeDc, Integer order, Boolean enabled, String etc1, String etc2, String etc3, String etc4, String etc5) {
        this.commonCodeId = commonCodeId;
        this.detailCode = detailCode;
        this.detailCodeNm = detailCodeNm;
        this.detailCodeEngNm = detailCodeEngNm;
        this.detailCodeDc = detailCodeDc;
        this.order = order;
        this.enabled = enabled;
        this.etc1 = etc1;
        this.etc2 = etc2;
        this.etc3 = etc3;
        this.etc4 = etc4;
        this.etc5 = etc5;
    }
}
