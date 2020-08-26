package com.project.component.code.packet;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * Created by KMS on 26/03/2020.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@ApiModel("공통 메인 코드 등록 객체")
public class ReqCommonCodeRegist {
    @NotEmpty
    @ApiModelProperty("공통그룹코드 (식별을 위한 하위 그룹코드와 동일한 영문단어 또는 이니셜)")
    private String groupCode;
    @NotEmpty
    @ApiModelProperty("공통코드명")
    private String codeNm;
    @NotEmpty
    @ApiModelProperty("공통코드 영문명")
    private String codeEngNm;
    @ApiModelProperty("공통코드 설명")
    private String codeDc;
    @ApiModelProperty("상위공통코드 ID")
    private Integer upperId;
    @ApiModelProperty("정렬순서")
    private Integer order;

    @Builder
    public ReqCommonCodeRegist(String groupCode, String codeNm, String codeEngNm, String codeDc, Integer upperId, Integer order) {
        this.groupCode = groupCode;
        this.codeNm = codeNm;
        this.codeEngNm = codeEngNm;
        this.codeDc = codeDc;
        this.upperId = upperId;
        this.order = order;
    }
}
