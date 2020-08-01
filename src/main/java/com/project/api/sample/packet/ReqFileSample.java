package com.project.api.sample.packet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by KMS on 08/05/2020.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@ApiModel("파일 업로드 요청 객체")
public class ReqFileSample extends ReqJsonFileSample {
    @ApiModelProperty(value = "업로드 단건파일", position = 3)
    private MultipartFile file;
    @ApiModelProperty(value = "업로드 다중파일", position = 4)
    private MultipartFile[] files;

    @Builder
    public ReqFileSample(String name, String phoneNumber, MultipartFile file, MultipartFile[] files) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.file = file;
        this.files = files;
    }
}