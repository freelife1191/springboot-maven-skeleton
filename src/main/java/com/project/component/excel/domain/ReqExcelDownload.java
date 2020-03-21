package com.project.component.excel.domain;

import lombok.*;

/**
 * 엑셀 다운로드 확장 도메인
 * Created by KMS on 04/09/2019.
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class ReqExcelDownload {
    /* 엑셀 제목 */
    private String title;
    /* 엑셀 Header */
    private String[] header = new String[]{};
    /* 엑셀 fileName */
    private String fileName;
    /* 엑셀 컬럼 사이즈(지정하지 않으면 기본 사이즈(3000)가 지정된다) */
    private Integer columnWidth;

    /* 스타일 여부 기본 false */
    private boolean style = false;
    /* 오토 컬럼 리사이징 사용 여부 기본 false */
    private boolean autoSize = false;

    @Builder
    public ReqExcelDownload(String title, String[] header, String fileName, Integer columnWidth, boolean style, boolean autoSize) {
        this.title = title;
        this.header = header;
        this.fileName = fileName;
        this.columnWidth = columnWidth;
        this.style = style;
        this.autoSize = autoSize;
    }
}
