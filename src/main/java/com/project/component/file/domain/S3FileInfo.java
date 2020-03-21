package com.project.component.file.domain;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Created by KMS on 26/08/2019.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class S3FileInfo {
    private String bucketName;
    private String key;
    private long size;
    private LocalDateTime lastModified;
    private String storageClass;
}