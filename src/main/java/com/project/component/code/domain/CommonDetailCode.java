package com.project.component.code.domain;

import com.project.component.code.packet.ReqCommonDetailCodeMod;
import com.project.component.code.packet.ReqCommonDetailCodeRegist;
import com.project.component.code.packet.ReqCommonDetailCodeRegistOne;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Created by KMS on 25/03/2020.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@ApiModel("공통 상세 코드 도메인 객체")
public class CommonDetailCode {
    @ApiModelProperty("공통코드 ID")
    private Integer commonCodeId;
    @ApiModelProperty("공통상세코드")
    private Integer detailCode;
    @ApiModelProperty("공통상세코드 명")
    private String detailCodeNm;
    @ApiModelProperty("공통상세코드 영문명")
    private String detailCodeEngNm;
    @ApiModelProperty("공통상세코드 설명")
    private String detailCodeDc;
    @ApiModelProperty("정렬순서")
    private Integer order;
    @ApiModelProperty(value = "사용여부", example = "true")
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
    @ApiModelProperty("생성자")
    private String createdId;
    @ApiModelProperty("생성일시")
    private LocalDateTime createdAt;
    @ApiModelProperty("수정자")
    private String updatedId;
    @ApiModelProperty("수정일시")
    private LocalDateTime updatedAt;

    /**
     * 공통 상세 코드 단건 등록 객체 셋팅
     * @param regist
     */
    public CommonDetailCode(ReqCommonDetailCodeRegistOne regist) {
        this.commonCodeId = regist.getCommonCodeId();
        this.detailCodeNm = regist.getDetailCodeNm();
        this.detailCodeEngNm = regist.getDetailCodeEngNm();
        this.detailCodeDc = regist.getDetailCodeDc();
        this.order = regist.getOrder();
        this.etc1 = regist.getEtc1();
        this.etc2 = regist.getEtc2();
        this.etc3 = regist.getEtc3();
        this.etc4 = regist.getEtc4();
        this.etc5 = regist.getEtc5();
    }

    /**
     * 공통 상세 코드 다중 등록 객체 셋팅
     * @param regist
     */
    public CommonDetailCode(Integer commonCodeId, ReqCommonDetailCodeRegist regist) {
        this.commonCodeId = commonCodeId;
        this.detailCodeNm = regist.getDetailCodeNm();
        this.detailCodeEngNm = regist.getDetailCodeEngNm();
        this.detailCodeDc = regist.getDetailCodeDc();
        this.order = regist.getOrder();
        this.etc1 = regist.getEtc1();
        this.etc2 = regist.getEtc2();
        this.etc3 = regist.getEtc3();
        this.etc4 = regist.getEtc4();
        this.etc5 = regist.getEtc5();
    }

    /**
     * 공통 상세 코드 수정 객체 셋팅
     * @param mod
     */
    public CommonDetailCode(ReqCommonDetailCodeMod mod) {
        this.commonCodeId = mod.getCommonCodeId();
        this.detailCode = mod.getDetailCode();
        this.detailCodeNm = mod.getDetailCodeNm();
        this.detailCodeEngNm = mod.getDetailCodeEngNm();
        this.detailCodeDc = mod.getDetailCodeDc();
        this.order = mod.getOrder();
        this.enabled = mod.getEnabled();
        this.etc1 = mod.getEtc1();
        this.etc2 = mod.getEtc2();
        this.etc3 = mod.getEtc3();
        this.etc4 = mod.getEtc4();
        this.etc5 = mod.getEtc5();
    }
}
