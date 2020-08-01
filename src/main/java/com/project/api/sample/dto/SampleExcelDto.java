package com.project.api.sample.dto;

import com.project.component.excel.utils.ExcelUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.poi.ss.usermodel.Row;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @ApiModelProperty(value = "이름", position = 1)
    private String name;
    @ApiModelProperty(value = "이메일", position = 2)
    private String email;
    @NotEmpty//(message = "전화번호는 필수 입력값입니다")
    @ApiModelProperty(value = "전화번호", position = 3)
    private String phone;
    @NotEmpty//(message = "소속부서는 필수 입력값입니다")
    @ApiModelProperty(value = "소속부서", position = 4)
    @Size(min = 1, max = 10)
    private String dept;
    @NotNull//(message = "업무코드는 필수 입력값입니다")
    @ApiModelProperty(value = "업무코드", position = 5)
    private Integer workCode;
    @NotNull//(message = "부서코드는 필수 입력값입니다")
    @ApiModelProperty(value = "부서코드", position = 6)
    private Integer deptCode;
    @Size(min = 1, max = 500)
    @ApiModelProperty(value = "내용", position = 7)
    private String content;
    @Size(max = 15)
    @Pattern(regexp="(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])", message="IP 주소 형식이 올바르지 않습니다")
    @ApiModelProperty(value = "IP", position = 8)
    private String ip;
    @ApiModelProperty(value = "소수", position = 9)
    private Double percent;
    @ApiModelProperty(value = "날짜", position = 10)
    private String createDate;
    @ApiModelProperty(value = "일시", position = 11)
    private String updateDatetime;
    @ApiModelProperty(value = "빈데이터", position = 12)
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
