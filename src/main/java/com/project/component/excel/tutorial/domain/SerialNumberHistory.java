package com.project.component.excel.tutorial.domain;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Created by KMS on 03/09/2019.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class SerialNumberHistory {
    private String serialNumber;
    /* 이력일자*/
    private LocalDateTime historyDate;
    /* 주문번호 */
    private String orderNumber;
    /* 프로젝트코드 */
    private String projectCode;
    /* 요청자수 */
    private String requestSeries;
    /* 등록자 */
    private String registrationEmployeeNumber;

    @Builder
    public SerialNumberHistory(String serialNumber, LocalDateTime historyDate, String orderNumber, String projectCode, String requestSeries, String registrationEmployeeNumber) {
        this.serialNumber = serialNumber;
        this.historyDate = historyDate;
        this.orderNumber = orderNumber;
        this.projectCode = projectCode;
        this.requestSeries = requestSeries;
        this.registrationEmployeeNumber = registrationEmployeeNumber;
    }

    public static SerialNumberHistory of(String serialNumber, LocalDateTime historyDate, String orderNumber, String projectCode, String requestSeries, String registrationEmployeeNumber) {
        return SerialNumberHistory.builder()
                .serialNumber(serialNumber)
                .historyDate(historyDate)
                .orderNumber(orderNumber)
                .projectCode(projectCode)
                .requestSeries(requestSeries)
                .registrationEmployeeNumber(registrationEmployeeNumber)
                .build();
    }
}