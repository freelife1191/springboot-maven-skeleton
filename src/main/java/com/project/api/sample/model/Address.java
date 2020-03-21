package com.project.api.sample.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * Created by KMS on 25/11/2019.
 * 도메인에 공통 적으로 사용되는 객체는 모델에 정의
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel("샘플 모델 객체") // 모델명
@JsonInclude(Include.ALWAYS)
public class Address {

    @NotEmpty
    @ApiModelProperty(value = "국가", notes = "국가 입력", example = "한국")
    //@JsonProperty("country")
    private String country;

    @NotEmpty
    @ApiModelProperty(value = "시도", notes = "시도 입력", example = "서울")
    //@JsonProperty("state")
    private String state;

    @NotEmpty
    @ApiModelProperty(value = "면읍구군시", notes = "면읍구군시 입력", example = "영등포구")
    //@JsonProperty("city")
    private String city;

    @NotEmpty
    @ApiModelProperty(value = "우편번호", name = "zip_code", notes = "우편번호 입력", example = "07276")
    @JsonProperty("zip_code")
    private String zipCode;

    @Builder
    public Address(final String country, final String state, final String city, final String zipCode) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.zipCode = zipCode;
    }

    /*
    public String getFullAddress() {
        return String.format("%s %s %s", this.state, this.city, this.zipCode);
    }
    */
}