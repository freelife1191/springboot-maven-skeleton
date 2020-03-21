package com.project.component.file.domain;

import lombok.*;

/**
 * Created by KMS on 26/08/2019.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UploadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
