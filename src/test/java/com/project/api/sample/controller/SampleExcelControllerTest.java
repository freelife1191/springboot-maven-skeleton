package com.project.api.sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.sample.dto.SampleExcelDto;
import com.project.common.BaseControllerTest;
import com.project.common.domain.CommonResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Sample Excel Controller 테스트
 * Created by KMS on 18/03/2020.
 */
class SampleExcelControllerTest extends BaseControllerTest {

    private static Validator validator;

    public SampleExcelControllerTest(ObjectMapper mapper, WebApplicationContext ctx) {
        super(mapper, ctx);
    }

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Sample 엑셀 다운로드")
    void getSampleExcel() throws Exception {
        mockMvc.perform(get("/sample/excel"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Sample 엑셀 업로드")
    void insertEquipment() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/sample/excel")
                .file(getMultipartFile("sampleUpload.xlsx", "file"))
                .header("jwt",jwtData);

        ResultActions result = mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(100))
                .andDo(print());

        String responseBody = result.andReturn().getResponse().getContentAsString();

        CommonResult commonResult = mapper.readValue(responseBody, CommonResult.class);
        List<Map<String, Object>> sampleExcelList = (List<Map<String, Object>>) commonResult.getResult();
        System.out.println(commonResult.getResult().getClass().getSimpleName());
        for(Map<String, Object> dto : sampleExcelList) {
            System.out.println(dto);
        }

    }

    @Test
    @DisplayName("Sample 엑셀 업로드 에러")
    void insertEquipmentError() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/sample/excel")
                .file(getMultipartFile("sampleUpload_error.xlsx", "file"))
                .header("jwt",jwtData);

        ResultActions result = mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(4011))
                .andDo(print());

        String responseBody = result.andReturn().getResponse().getContentAsString();

        CommonResult commonResult = mapper.readValue(responseBody, CommonResult.class);
        List<Map<String, Object>> sampleExcelList = (List<Map<String, Object>>) commonResult.getResult();
        System.out.println(commonResult.getResult().getClass().getSimpleName());
        for(Map<String, Object> dto : sampleExcelList) {
            System.out.println(dto);
        }

    }

    @Test
    @DisplayName("객체 Validation 체크")
    void objectValidationCheck() {
        SampleExcelDto dto = new SampleExcelDto();
        Set<ConstraintViolation<SampleExcelDto>> constraintValidations = validator.validate(dto);

        ConstraintViolation<SampleExcelDto> validData = constraintValidations.stream()
                .filter(data -> data.getPropertyPath().toString().equals("name"))
                .findFirst().orElse(null);

        if(Objects.nonNull(validData)){
            System.out.println(validData.getMessageTemplate().contains("NotEmpty") || validData.getMessageTemplate().contains("NotNull"));
            // System.out.println(validData.getPropertyPath()+" : "+validData.getMessage());
        }



        // for(ConstraintViolation data : constraintValidations) {
        //     System.out.println(data.getPropertyPath());
        // }


        // System.out.println("## constraintValidations = "+constraintValidations);
        // System.out.println("## size = "+constraintValidations.size());
        // System.out.println("## message = "+constraintValidations.iterator().next().getMessage());
    }

}