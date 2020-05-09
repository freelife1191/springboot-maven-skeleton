package com.project.api.sample.packet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by KMS on 08/05/2020.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel("파일 업로드 요청 객체")
public class ReqJsonFileSample {

    @ApiModelProperty(value = "이름", example = "아이언맨")
    protected String name;
    @ApiModelProperty(value = "전화번호", example = "010-1111-1111")
    protected String phoneNumber;

}