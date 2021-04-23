package com.java;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.sql.Timestamp;

/**
 * Created by KMS on 17/04/2020.
 */
public class LocalDateTimeTest {

    @Test
    public void DateTimeParsingTest() {
        System.out.println(new Timestamp(1580707730000L).toLocalDateTime());
    }
}
