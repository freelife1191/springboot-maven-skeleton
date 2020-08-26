package com.project.component.code.domain;

import com.project.component.code.packet.ReqCommonDetailCodeMod;
import com.project.component.code.packet.ReqCommonDetailCodeRegist;
import com.project.component.code.packet.ReqCommonDetailCodeRegistOne;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 공통 상세 코드 도메인
 * Created by KMS on 25/03/2020.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CommonDetailCode {
    /** 공통코드 ID */
    private Integer commonCodeId;
    /** 공통상세코드 */
    private Integer detailCode;
    /** 공통상세코드 명 */
    private String detailCodeNm;
    /** 공통상세코드 영문명 */
    private String detailCodeEngNm;
    /** 공통상세코드 설명 */
    private String detailCodeDc;
    /** 정렬순서 */
    private Integer order;
    /** 사용여부 */
    private Boolean enabled;
    /** 기타1 */
    private String etc1;
    /** 기타2 */
    private String etc2;
    /** 기타3 */
    private String etc3;
    /** 기타4 */
    private String etc4;
    /** 기타5 */
    private String etc5;
    /** 생성자 */
    private String createdId;
    /** 생성일시 */
    private LocalDateTime createdAt;
    /** 수정자 */
    private String updatedId;
    /** 수정일시 */
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
