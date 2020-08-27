package com.project.component.code.packet;

import com.project.common.annotation.ParamName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * Created by KMS on 27/08/2020.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@ApiModel("공통 메인 코드 조회 요청")
public class ReqCommonCodeGET {
    @ApiModelProperty(value = "공통코드 ID")
    private Integer id;
    @ApiModelProperty(value = "공통그룹코드 (식별을 위한 하위 그룹코드와 동일한 영문단어 또는 이니셜)", name = "group_code")
    @ParamName("group_code")
    private String groupCode;
    @ApiModelProperty(value = "공통코드(공통코드 ID 하위의 단순증가값)")
    private Integer code;
    @ApiModelProperty(value = "공통코드명", name = "code_nm")
    @ParamName("code_nm")
    private String codeNm;
    @ApiModelProperty(value = "공통코드 영문명", name = "code_eng_nm")
    @ParamName("code_eng_nm")
    private String codeEngNm;
    @ApiModelProperty(value = "공통코드 설명", name = "code_dc")
    @ParamName("code_dc")
    private String codeDc;
    @ApiModelProperty(value = "상위공통코드 ID", name = "upper_id")
    @ParamName("upper_id")
    private Integer upperId;
    @ApiModelProperty("뎁스")
    private Integer depth;
    @ApiModelProperty("정렬순서")
    private Integer order;
    @ApiModelProperty(value = "사용여부", example = "true")
    private Boolean enabled;
}
