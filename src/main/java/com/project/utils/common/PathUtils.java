package com.project.utils.common;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.Objects;

/**
 * 경로 유틸리티
 * Created by KMS on 24/02/2020.
 */
public class PathUtils {

    /**
     * 잘못된 경로 정정 후 리턴
     * @param path
     * @return
     */
    public static String getPath(Path path) {
        if(Objects.isNull(path)) return null;
        return getPath(String.valueOf(path));
    }

    /**
     * 잘못된 경로 정정 후 리턴
     * @param path
     * @return
     */
    public static String getPath(String path) {
        if(StringUtils.isEmpty(path)) return null;
        return StringUtils.replace(path, "\\", "/");
    }
}
