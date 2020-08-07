package com.project.common.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.utils.common.TestUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;

/**
 * Created by KMS on 06/08/2020.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class MultiValueMapConverter {

    public static MultiValueMap<String, String> convert(ObjectMapper objectMapper, Object dto) { // (2)
        try {
            MultiValueMap<String, String> params;
            // Map<String, String> map = objectMapper.convertValue(dto, new TypeReference<>() {}); // (3)
            // params.setAll(map); // (4)
            params = TestUtils.objectToMultiValueMap(dto);
            return params;
        } catch (Exception e) {
            log.error("Url Parameter 변환중 오류가 발생했습니다. requestDto={}", dto, e);
            throw new IllegalStateException("Url Parameter 변환중 오류가 발생했습니다.");
        }
    }
}
