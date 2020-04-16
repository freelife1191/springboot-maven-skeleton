package com.project.component.code.domain;

import com.project.component.code.packet.ReqCommonCodeMod;
import com.project.component.code.packet.ReqCommonCodeRegist;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("공통 메인 코드 도메인 객체")
public class CommonCode {
    @ApiModelProperty("공통코드 ID")
    private Integer id;
    @ApiModelProperty("공통그룹코드 (식별을 위한 하위 그룹코드와 동일한 영문단어 또는 이니셜)")
    private String groupCode;
    @ApiModelProperty("공통코드(공통코드 ID 하위의 단순증가값)")
    private Integer code;
    @ApiModelProperty("공통코드명")
    private String codeNm;
    @ApiModelProperty("공통코드 영문명")
    private String codeEngNm;
    @ApiModelProperty("공통코드 설명")
    private String codeDc;
    @ApiModelProperty("상위공통코드 ID")
    private Integer upperId;
    @ApiModelProperty("뎁스")
    private Integer depth;
    @ApiModelProperty("정렬순서")
    private Integer order;
    @ApiModelProperty("공통코드 ID 경로")
    private String path ;
    @ApiModelProperty("공통코드명 경로")
    private String pathNm;
    @ApiModelProperty(value = "사용여부", example = "true")
    private Boolean enabled;
    @ApiModelProperty(value = "생성자")
    private String createdId;
    @ApiModelProperty(value = "생성일시")
    private LocalDateTime createdAt;
    @ApiModelProperty(value = "수정자")
    private String updatedId;
    @ApiModelProperty(value = "수정일시")
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
