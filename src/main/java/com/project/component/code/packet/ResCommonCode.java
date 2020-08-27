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
 * Created by KMS on 27/08/2020.
 */
@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@ApiModel("공통 메인 코드 조회 응답")
public class ResCommonCode {
    @ApiModelProperty(value = "공통코드 ID", position = 1)
    private Integer id;
    @ApiModelProperty(value = "공통그룹코드 (식별을 위한 하위 그룹코드와 동일한 영문단어 또는 이니셜)", position = 2)
    private String groupCode;
    @ApiModelProperty(value = "공통코드(공통코드 ID 하위의 단순증가값)", position = 3)
    private Integer code;
    @ApiModelProperty(value = "공통코드명", position = 4)
    private String codeNm;
    @ApiModelProperty(value = "공통코드 영문명", position = 5)
    private String codeEngNm;
    @ApiModelProperty(value = "공통코드 설명", position = 6)
    private String codeDc;
    @ApiModelProperty(value = "상위공통코드 ID", position = 7)
    private Integer upperId;
    @ApiModelProperty(value = "뎁스", position = 8)
    private Integer depth;
    @ApiModelProperty(value = "정렬순서", position = 9)
    private Integer order;
    @ApiModelProperty(value = "공통코드 ID 경로", position = 10)
    private String path ;
    @ApiModelProperty(value = "공통코드명 경로", position = 11)
    private String pathNm;
    @ApiModelProperty(value = "사용여부", position = 12, example = "true")
    private Boolean enabled;
    @ApiModelProperty(value = "생성자", position = 13)
    private String createdId;
    @ApiModelProperty(value = "생성일시", position = 14)
    private LocalDateTime createdAt;
    @ApiModelProperty(value = "수정자", position = 15)
    private String updatedId;
    @ApiModelProperty(value = "수정일시", position = 16)
    private LocalDateTime updatedAt;
}
