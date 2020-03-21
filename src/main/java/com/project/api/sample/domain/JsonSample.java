package com.project.api.sample.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.api.sample.model.Address;
import com.project.api.sample.packet.ReqJsonSample;
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
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@ApiModel("샘플 POST RequestBody 객체 도메인") // 모델명
@JsonInclude(Include.ALWAYS)
public class JsonSample {
    @ApiModelProperty(value = "순번", notes = "순번 데이터", example = "1")
    protected Integer seq;
    @NotNull
    /*
        @ApiModelProperty 사용법
        value - 파라메터 이름
        name - Swagger에서 입력 받을 리얼 파라메터 명
        notes - 부가설명
        example - 지정된 임의 테스트 값을 입력 함
        required - 필수 입력 여부

     */
    @ApiModelProperty(value = "아이디",name = "my_id", notes = "이곳에 아이디를 넣어주세요", example = "abcd", required = true)
    @JsonProperty("my_id")
    protected String myId;

    @NotNull
    @ApiModelProperty(value = "이름",name = "my_name", notes = "이곳에 이름을 넣어주세요", example = "홍길동", required = true)
    @JsonProperty("my_name")
    protected String myName;

    @Min(10)
    @ApiModelProperty(value = "나이\n최소 10살이상",name = "my_age", notes = "나이는 최소 10살이상 입니다", example = "10")
    @JsonProperty("my_age")
    protected Integer myAge;

    @Email
    @ApiModelProperty(value = "이메일\n이메일 형식에 맞게 입력",name = "my_email", notes = "이메일 형식에 맞게 입력해주세요",example = "abc@company.com")
    @JsonProperty("my_email")
    protected String myEmail;

    @NotNull
    @ApiModelProperty(value = "직업",name = "my_job", notes = "직업을 입력하세요", example = "developer", required = true)
    @JsonProperty("my_job")
    protected String myJob;

    @ApiModelProperty(value = "주소",name = "my_address", notes = "county, state, city, zipCode")
    @JsonProperty("my_address")
    protected Address myAddress;

    public JsonSample(ReqJsonSample reqJsonSample) {
        this.seq = reqJsonSample.getSeq();
        this.myId = reqJsonSample.getMyId();
        this.myName = reqJsonSample.getMyName();
        this.myAge = reqJsonSample.getMyAge();
        this.myEmail = reqJsonSample.getMyEmail();
        this.myJob = reqJsonSample.myJob;
        this.myAddress = reqJsonSample.myAddress;
    }
}
