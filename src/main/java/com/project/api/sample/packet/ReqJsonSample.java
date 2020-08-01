package com.project.api.sample.packet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.project.api.sample.constant.Hobby;
import com.project.api.sample.domain.JsonSample;
import com.project.api.sample.model.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by KMS on 14/11/2019.
 * 샘플 객체
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel("샘플 POST 요청 RequestBody 객체") // 모델명
@JsonInclude(Include.ALWAYS)
public class ReqJsonSample extends JsonSample {

    @ApiModelProperty(value = "취미", position = 8)
    private Hobby hobby;

    @Builder
    public ReqJsonSample(Integer seq, @NotNull String myId, @NotNull String myName, @Min(10) Integer myAge, @Email String myEmail, @NotNull String myJob, Address myAddress, Hobby hobby) {
        super(seq, myId, myName, myAge, myEmail, myJob, myAddress);
        this.hobby = hobby;
    }
}
