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
@Builder
@ApiModel("샘플 파일 업로드 응답 객체")
public class ResFileSample {

    @ApiModelProperty(value = "이름", position = 1)
    private String name;
    @ApiModelProperty(value = "전화번호", position = 2)
    private String phoneNumber;
    @ApiModelProperty(value = "파일명", position = 3)
    private String fileName;
    @ApiModelProperty(value = "파일다운로드URL", position = 4)
    private String fileDownloadUri;
    @ApiModelProperty(value = "파일타입", position = 5)
    private String fileType;
    @ApiModelProperty(value = "파일사이즈", position = 6)
    private long size;
}
