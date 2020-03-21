package com.project.component.excel.tutorial.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.ss.usermodel.Row;

/**
 * Created by KMS on 02/09/2019.
 * 테스트 회사 객체
 */
@Setter
@Getter
@ToString
public class Company {
    private String name;
    private String email;
    private String telNo;
    private String workCode;
    private String deptCode;
    private String etc;

    @Builder
    public Company(String name, String email, String telNo, String workCode, String deptCode, String etc) {
        this.name = name;
        this.email = email;
        this.telNo = telNo;
        this.workCode = workCode;
        this.deptCode = deptCode;
        this.etc = etc;
    }

    /**
     * 엑셀 값 매핑
     * @param row
     * @return
     */
    public static Company from(Row row) {
        return new Company(
                row.getCell(0).getStringCellValue(), // name
                row.getCell(1).getStringCellValue(), // email
                row.getCell(2).getStringCellValue(), // telNo
                row.getCell(3).getStringCellValue(), // workCode
                row.getCell(4).getStringCellValue(), // deptCode
                row.getCell(5).getStringCellValue()  // etc
        );
    }
}
