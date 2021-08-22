package com.project.api.sample.packet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by KMS on 2021/04/26.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@ApiModel("파일 업로드 요청 객체")
public class ReqFileDataSample {
    @ApiModelProperty(value = "데이터")
    private String data;
    @ApiModelProperty(value = "업로드 단건파일", position = 3)
    private MultipartFile file;
}
