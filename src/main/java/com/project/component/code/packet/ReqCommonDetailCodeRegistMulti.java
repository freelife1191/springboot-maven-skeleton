package com.project.component.code.packet;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * Created by KMS on 30/03/2020.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@ApiModel("공통 상세 코드 다중 등록 객체")
public class ReqCommonDetailCodeRegistMulti extends ReqCommonDetailCodeRegist{

    @Builder
    public ReqCommonDetailCodeRegistMulti(String detailCodeNm, String detailCodeEngNm, String detailCodeDc, Integer order, String etc1, String etc2, String etc3, String etc4, String etc5) {
        super(detailCodeNm, detailCodeEngNm, detailCodeDc, order, etc1, etc2, etc3, etc4, etc5);
    }
}
