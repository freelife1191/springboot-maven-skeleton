package com.project.api.sample.domain;

import com.project.api.sample.model.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * Created by KMS on 14/11/2019.
 * 샘플 도메인 객체
 * DB 컬럼과 일치하는 도메인 객체를 생성
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@ApiModel("샘플 Domain 객체") // 모델명
public class Sample {
    /*
        @ApiModelProperty 사용법
        value - 파라메터 이름
        name - Swagger에서 입력 받을 리얼 파라메터 명
        notes - 부가설명
        example - 지정된 임의 테스트 값을 입력 함
        required - 필수 입력 여부

     */
    @ApiModelProperty(value = "아이디", position = 1, notes = "이곳에 아이디를 넣어주세요", example = "abcd", required = true)
    private String myId;

    @ApiModelProperty(value = "이름", position = 2, notes = "이곳에 이름을 넣어주세요", example = "홍길동", required = true)
    private String myName;

    @ApiModelProperty(value = "나이\n최소 10살이상", position = 3, notes = "나이는 최소 10살이상 입니다", example = "10")
    private Integer myAge;

    @ApiModelProperty(value = "이메일\n이메일 형식에 맞게 입력", position = 4, notes = "이메일 형식에 맞게 입력",example = "abc@company.com")
    private String myEmail;

    @ApiModelProperty(value = "직업", position = 5, notes = "농부, 광부, 개발자", example = "developer", required = true)
    private String myJob;

    @ApiModelProperty(value = "주소", position = 6, notes = "county, state, city, zipCode")
    private Address myAddress;
}
