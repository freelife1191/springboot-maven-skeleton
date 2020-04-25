package com.java;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.utils.common.CommonUtils;
import com.project.utils.common.JsonUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KMS on 21/02/2020.
 */
public class BigDecimalTests {

    @Test
    public void BigDecimalMapping() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("lo", 123.1234567812312312313);

        String jsonData = mapper.writeValueAsString(dataMap);
//        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        System.out.println(JsonUtils.toPrettyJson(jsonData));

        Map<String, Object> resultMap = mapper.readValue(jsonData, Map.class);
        System.out.println(resultMap.get("lo").getClass().getTypeName());
        System.out.println(resultMap.get("lo"));

        BigDecimal bigDecimal = new BigDecimal(resultMap.get("lo").toString());
//        bigDecimal = 123.12345678;
//        System.out.println(bigDecimal);
    }

    @Test
    //https://jsonobject.tistory.com/466
    public void BigDecimalTransfer() {
        String lat = "-62.22328130000000";

        BigDecimal decimal = new BigDecimal(lat).setScale(14, RoundingMode.DOWN);
        System.out.println(decimal.compareTo(BigDecimal.ZERO) > 0);
        //곱하기
        decimal = decimal.multiply(new BigDecimal(-1));
        System.out.println(decimal);
    }

    @Test
    public void strToBigDecimalEmptyIfNullTest() {
        String numStr = "1.3655";
        System.out.println(CommonUtils.strToBigDecimalEmptyIfNull(numStr,3));
    }
}
