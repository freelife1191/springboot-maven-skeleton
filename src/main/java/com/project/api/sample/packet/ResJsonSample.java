package com.project.api.sample.packet;

import com.project.api.sample.constant.Hobby;
import com.project.api.sample.domain.JsonSample;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by KMS on 12/03/2020.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel("샘플 Json 응답 객체") // 모델명
public class ResJsonSample extends JsonSample {

    @ApiModelProperty("취미")
    private Hobby hobby;

    public ResJsonSample(ReqJsonSample reqJsonSample) {
        super(reqJsonSample);
        this.hobby = reqJsonSample.getHobby();
    }
}
