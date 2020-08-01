package com.project.api.sample.packet;

import com.project.api.sample.constant.Hobby;
import com.project.api.sample.domain.GetSample;
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
@ApiModel("샘플 Get 응답 객체") // 모델명
public class ResGetSample extends GetSample {

    @ApiModelProperty(value = "취미", position = 8)
    private Hobby hobby;

    public ResGetSample(ReqGetSample reqGetSample) {
        super(reqGetSample);
        this.hobby = reqGetSample.getHobby();
    }
}
