package com.project.component.code.packet;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Created by KMS on 25/08/2020.
 */
@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@ApiModel("공통 상세 코드 조회 응답")
public class ResCommonDetailCode {

    @ApiModelProperty(value = "공통코드 ID", position = 1)
    private Integer commonCodeId;
    @ApiModelProperty(value = "공통상세코드", position = 2)
    private Integer detailCode;
    @ApiModelProperty(value = "공통상세코드 명", position = 3)
    private String detailCodeNm;
    @ApiModelProperty(value = "공통상세코드 영문명", position = 4)
    private String detailCodeEngNm;
    @ApiModelProperty(value = "공통상세코드 설명", position = 5)
    private String detailCodeDc;
    @ApiModelProperty(value = "정렬순서", position = 6)
    private Integer order;
    @ApiModelProperty(value = "사용여부", example = "true", position = 7)
    private Boolean enabled;
    @ApiModelProperty(value = "기타1", position = 8)
    private String etc1;
    @ApiModelProperty(value = "기타2", position = 9)
    private String etc2;
    @ApiModelProperty(value = "기타3", position = 10)
    private String etc3;
    @ApiModelProperty(value = "기타4", position = 11)
    private String etc4;
    @ApiModelProperty(value = "기타5", position = 12)
    private String etc5;
    @ApiModelProperty(value = "생성자", position = 13)
    private String createdId;
    @ApiModelProperty(value = "생성일시", position = 14)
    private LocalDateTime createdAt;
    @ApiModelProperty(value = "수정자", position = 15)
    private String updatedId;
    @ApiModelProperty(value = "수정일시", position = 16)
    private LocalDateTime updatedAt;

}
