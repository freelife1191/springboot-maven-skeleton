package com.project.component.code.packet;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by KMS on 26/03/2020.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@ApiModel("공통 메인 코드 수정 객체")
public class ReqCommonCodeMod {
    @ApiModelProperty(value = "공통코드 ID", hidden = true)
    private Integer id;
    @ApiModelProperty("공통그룹코드 (식별을 위한 하위 그룹코드와 동일한 영문단어 또는 이니셜)")
    private String groupCode;
    @ApiModelProperty("공통코드명")
    private String codeNm;
    @ApiModelProperty("공통코드 영문명")
    private String codeEngNm;
    @ApiModelProperty("공통코드 설명")
    private String codeDc;
    @ApiModelProperty("상위공통코드 ID")
    private Integer upperId;
    @ApiModelProperty("정렬순서")
    private Integer order;
    @ApiModelProperty("사용여부")
    private Boolean enabled;
}
