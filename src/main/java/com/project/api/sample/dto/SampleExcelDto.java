package com.project.api.sample.dto;

import com.project.component.excel.utils.ExcelUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.poi.ss.usermodel.Row;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 엑셀 업로드 Sample DTO
 * Created by KMS on 02/09/2019.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel("Sample 엑셀 업로드 객체")
public class SampleExcelDto {
    @NotEmpty//(message = "이름은 필수 입력값 입니다")
    @ApiModelProperty("이름")
    private String name;
    @ApiModelProperty("이메일")
    private String email;
    @NotEmpty//(message = "전화번호는 필수 입력값입니다")
    @ApiModelProperty("전화번호")
    private String phone;
    @NotEmpty//(message = "소속부서는 필수 입력값입니다")
    @ApiModelProperty("소속부서")
    @Size(min = 1, max = 10)
    private String dept;
    @NotNull//(message = "업무코드는 필수 입력값입니다")
    @ApiModelProperty("업무코드")
    private Integer workCode;
    @NotNull//(message = "부서코드는 필수 입력값입니다")
    @ApiModelProperty("부서코드")
    private Integer deptCode;
    @Size(min = 1, max = 500)
    @ApiModelProperty("내용")
    private String content;
    @ApiModelProperty("소수")
    private Double percent;
    @ApiModelProperty("날짜")
    private String createDate;
    @ApiModelProperty("일시")
    private String updateDatetime;
    @ApiModelProperty("빈데이터")
    private String emptyData;

    /**
     * 엑셀 업로드 처리를 위한 객체
     * @param row
     * @return
     */
    public static SampleExcelDto from(Row row) {
        return ExcelUtils.setObjectMapping(new SampleExcelDto(), row);
    }
}
