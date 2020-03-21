package com.project.utils.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by KMS on 08/12/2019.
 * DateTime Utils
 */
public class DateTimeUtils {

    /**
     * LocalDateTime yyyy-MM-ddTHH:mm:ss 형식을 yyyy-MM-dd HH:mm:ss 형식으로 변환
     * @param dateTime
     * @return
     */
    public static String getLocalDateTimeConverter(String dateTime) {
        return dateTime != null ? LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }

    /**
     * LocalDateTime yyyy-MM-ddTHH:mm:ss 형식을 yyyy-MM-dd HH:mm:ss 형식으로 변환
     * @param localDateTime
     * @return
     */
    public static String getLocalDateTimeConverter(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }

    /**
     * 날짜검색 시 조회 시작일시
     * @param date
     * @return
     */
    public static LocalDateTime searchDateTimeStart(String date) {
        return LocalDateTime.parse(DateUtils.getLocalDate(date)+" 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 날짜검색 시 조회 종료일시
     * @param date
     * @return
     */
    public static LocalDateTime searchDateTimeEnd(String date) {
        return LocalDateTime.parse(DateUtils.getLocalDate(date).plusDays(1)+" 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
