package com.project.component.code.domain;

import com.project.component.code.packet.ReqCommonCodeMod;
import com.project.component.code.packet.ReqCommonCodeRegist;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 공통 메인 코드 도메인 객체
 * Created by KMS on 25/03/2020.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommonCode {
    /** 공통코드 ID */
    private Integer id;
    /** 공통그룹코드 (식별을 위한 하위 그룹코드와 동일한 영문단어 또는 이니셜) */
    private String groupCode;
    /** 공통코드(공통코드 ID 하위의 단순증가값) */
    private Integer code;
    /** 공통코드명 */
    private String codeNm;
    /** 공통코드 영문명 */
    private String codeEngNm;
    /** 공통코드 설명 */
    private String codeDc;
    /** 상위공통코드 ID */
    private Integer upperId;
    /** 뎁스 */
    private Integer depth;
    /** 정렬순서 */
    private Integer order;
    /** 공통코드 ID 경로 */
    private String path ;
    /** 공통코드명 경로 */
    private String pathNm;
    /** 사용여부 */
    private Boolean enabled;
    /** 생성자 */
    private String createdId;
    /** 생성일시 */
    private LocalDateTime createdAt;
    /** 수정자 */
    private String updatedId;
    /** 수정일시 */
    private LocalDateTime updatedAt;

    @Builder
    public CommonCode(Integer id, String groupCode, Integer code, String codeNm, String codeEngNm, String codeDc, Integer upperId, Integer depth, Integer order, String path, String pathNm, Boolean enabled, String createdId, LocalDateTime createdAt, String updatedId, LocalDateTime updatedAt) {
        this.id = id;
        this.groupCode = groupCode;
        this.code = code;
        this.codeNm = codeNm;
        this.codeEngNm = codeEngNm;
        this.codeDc = codeDc;
        this.upperId = upperId;
        this.depth = depth;
        this.order = order;
        this.path = path;
        this.pathNm = pathNm;
        this.enabled = enabled;
        this.createdId = createdId;
        this.createdAt = createdAt;
        this.updatedId = updatedId;
        this.updatedAt = updatedAt;
    }


    /**
     * 공통 메인 코드 등록 객체 셋팅
     * @param regist
     */
    public CommonCode(ReqCommonCodeRegist regist) {
        this.groupCode = regist.getGroupCode();
        this.codeNm = regist.getCodeNm();
        this.codeEngNm = regist.getCodeEngNm();
        this.codeDc = regist.getCodeDc();
        this.upperId = regist.getUpperId();
        this.order = regist.getOrder();
    }

    /**
     * 공통 메인 코드 수정 객체 셋팅
     * @param mod
     */
    public CommonCode(ReqCommonCodeMod mod) {
        this.id = mod.getId();
        this.groupCode = mod.getGroupCode();
        this.codeNm = mod.getCodeNm();
        this.codeEngNm = mod.getCodeEngNm();
        this.codeDc = mod.getCodeDc();
        this.upperId = mod.getUpperId();
        this.order = mod.getOrder();
        this.enabled = mod.getEnabled();
    }

}
