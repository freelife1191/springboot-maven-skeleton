package com.project.component.code.packet;

import com.project.common.annotation.ParamName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Created by KMS on 25/08/2020.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@ApiModel("공통 상세 코드 조회 요청")
public class ReqCommonDetailCodeGET {

    @ApiModelProperty(value = "공통코드 ID", name = "common_code_id")
    @ParamName("common_code_id")
    private Integer commonCodeId;
    @ApiModelProperty(value = "공통상세코드", name = "detail_code")
    @ParamName("detail_code")
    private Integer detailCode;
    @ApiModelProperty(value = "공통상세코드 명", name = "detail_code_nm")
    @ParamName("detail_code_nm")
    private String detailCodeNm;
    @ApiModelProperty(value = "공통상세코드 영문명", name = "detail_code_eng_nm")
    @ParamName("detail_code_eng_nm")
    private String detailCodeEngNm;
    @ApiModelProperty(value = "공통상세코드 설명", name = "detail_code_dc")
    @ParamName("detail_code_dc")
    private String detailCodeDc;
    @ApiModelProperty(value = "정렬순서")
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
    @ApiModelProperty(value = "생성자", name = "created_id")
    @ParamName("created_id")
    private String createdId;
    @ApiModelProperty(value = "생성일시", name = "created_at")
    @ParamName("created_at")
    private LocalDateTime createdAt;
    @ApiModelProperty(value = "수정자", name = "updated_id")
    @ParamName("updated_id")
    private String updatedId;
    @ApiModelProperty(value = "수정일시", name = "updated_at")
    @ParamName("updated_at")
    private LocalDateTime updatedAt;
}
