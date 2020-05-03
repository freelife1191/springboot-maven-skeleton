package com.project.api.sample.packet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * created by KMS on 03/05/2020
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@ApiModel("샘플 파일 업로드 응답 객체")
public class ResFileSample {
    @ApiModelProperty("파일명")
    private String fileName;
    @ApiModelProperty("파일다운로드URL")
    private String fileDownloadUri;
    @ApiModelProperty("파일타입")
    private String fileType;
    @ApiModelProperty("파일사이즈")
    private long size;
}
