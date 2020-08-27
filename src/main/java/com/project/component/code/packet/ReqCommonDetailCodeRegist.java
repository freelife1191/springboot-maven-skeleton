package com.project.component.code.packet;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * Created by KMS on 27/03/2020.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@ApiModel("공통 상세 코드 등록 요청")
public class ReqCommonDetailCodeRegist {
    @NotEmpty(message = "공통상세코드 명은 필수 입력값 입니다")
    @ApiModelProperty(value = "공통상세코드 명", required = true)
    private String detailCodeNm;
    @NotEmpty(message = "공통상세코드 영문명은 필수 입력값 입니다")
    @ApiModelProperty(value = "공통상세코드 영문명", required = true)
    private String detailCodeEngNm;
    @ApiModelProperty("공통상세코드 설명")
    private String detailCodeDc;
    @ApiModelProperty("정렬순서")
    private Integer order;
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

    public ReqCommonDetailCodeRegist(@NotEmpty String detailCodeNm, @NotEmpty String detailCodeEngNm, String detailCodeDc, Integer order, String etc1, String etc2, String etc3, String etc4, String etc5) {
        this.detailCodeNm = detailCodeNm;
        this.detailCodeEngNm = detailCodeEngNm;
        this.detailCodeDc = detailCodeDc;
        this.order = order;
        this.etc1 = etc1;
        this.etc2 = etc2;
        this.etc3 = etc3;
        this.etc4 = etc4;
        this.etc5 = etc5;
    }
}
